package fr.delphes.bot

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.TwitchChat
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.chat.events.channel.IRCMessageEvent
import com.github.twitch4j.pubsub.events.ChannelBitsEvent
import fr.delphes.bot.command.Command
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.event.outgoing.TwitchOutgoingEvent
import fr.delphes.bot.state.ChannelState
import fr.delphes.bot.state.Statistics
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.bot.twitch.handler.ChannelBitsHandler
import fr.delphes.bot.twitch.handler.ChannelMessageHandler
import fr.delphes.bot.twitch.handler.ChannelUpdateHandler
import fr.delphes.bot.twitch.handler.IRCMessageHandler
import fr.delphes.bot.twitch.handler.NewFollowHandler
import fr.delphes.bot.twitch.handler.NewSubHandler
import fr.delphes.bot.twitch.handler.RewardRedeemedHandler
import fr.delphes.bot.twitch.handler.StreamOfflineHandler
import fr.delphes.bot.twitch.handler.StreamOnlineHandler
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.feature.Feature
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.ChannelTwitchClient
import fr.delphes.twitch.api.channelFollow.NewFollow
import fr.delphes.twitch.api.channelSubscribe.NewSub
import fr.delphes.twitch.api.channelUpdate.ChannelUpdate
import fr.delphes.twitch.api.reward.RewardRedemption
import fr.delphes.twitch.api.streamOffline.StreamOffline
import fr.delphes.twitch.api.streamOnline.StreamOnline
import fr.delphes.twitch.auth.AuthToken
import fr.delphes.twitch.auth.AuthTokenRepository
import fr.delphes.twitch.auth.TwitchUserCredential
import fr.delphes.twitch.model.Stream
import fr.delphes.utils.exhaustive
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

class Channel(
    configuration: ChannelConfiguration,
    val bot: ClientBot,
    private val state: ChannelState = ChannelState()
) : ChannelInfo {
    override val commands: List<Command> = configuration.features.flatMap(Feature::commands)
    override val currentStream: Stream? get() = state.currentStream
    override val statistics: Statistics get() = state.statistics
    val alerts = Channel<Alert>()

    val name = configuration.ownerChannel
    val userId : String
    val channelCredential = TwitchUserCredential.of(
        bot.appCredential,
        AuthTokenRepository(
            "${bot.configFilepath}\\auth\\channel-$name.json"
        )
    )

    private val oAuth = configuration.ownerAccountOauth

    val features = configuration.features
    private val ownerCredential = OAuth2Credential("twitch", oAuth)
    private val eventHandlers = EventHandlers()

    private val client: com.github.twitch4j.TwitchClient
    private val chat: TwitchChat

    val twitchApi: ChannelTwitchApi

    private val newFollowHandler: TwitchIncomingEventHandler<NewFollow> = NewFollowHandler()
    private val newSubHandler: TwitchIncomingEventHandler<NewSub> = NewSubHandler()
    private val channelBitsHandler: TwitchIncomingEventHandler<ChannelBitsEvent> = ChannelBitsHandler()
    private val rewardRedeemedHandler: TwitchIncomingEventHandler<RewardRedemption> = RewardRedeemedHandler()
    private val channelMessageHandler: TwitchIncomingEventHandler<ChannelMessageEvent> = ChannelMessageHandler()
    private val ircMessageHandler: TwitchIncomingEventHandler<IRCMessageEvent> = IRCMessageHandler()
    private val channelUpdateHandler: TwitchIncomingEventHandler<ChannelUpdate> = ChannelUpdateHandler()
    private val streamOnlineHandler: TwitchIncomingEventHandler<StreamOnline> = StreamOnlineHandler(this)
    private val streamOfflineHandler: TwitchIncomingEventHandler<StreamOffline> = StreamOfflineHandler()

    init {
        twitchApi = ChannelTwitchClient.builder(bot.appCredential, channelCredential, name, bot.publicUrl, bot.webhookSecret)
            .listenToReward { rewardRedeemedHandler.handleTwitchEvent(it) }
            .listenToNewFollow { newFollowHandler.handleTwitchEvent(it) }
            .listenToNewSub { newSubHandler.handleTwitchEvent(it) }
            .listenToStreamOnline { streamOnlineHandler.handleTwitchEvent(it) }
            .listenToStreamOffline { streamOfflineHandler.handleTwitchEvent(it) }
            .listenToChannelUpdate { channelUpdateHandler.handleTwitchEvent(it) }
            .build()
        userId = twitchApi.userId

        client = TwitchClientBuilder.builder()
            .withClientId(bot.appCredential.clientId)
            .withClientSecret(bot.appCredential.clientSecret)
            .withEnablePubSub(true)
            .withEnableChat(true)
            .withChatAccount(ownerCredential)
            .withDefaultAuthToken(channelCredential.toCredential())
            .build()!!

        chat = client.chat
        LOGGER.debug { "Retrieved owner user id : $userId" }

        features.forEach { feature ->
            feature.registerHandlers(eventHandlers)
        }

        runBlocking {
            state.init(twitchApi.getStream())
        }

        chat.connect()
        client.pubSub.connect()

        //TODO subscribe only when feature requires
        val eventHandler = client.eventManager.getEventHandler(SimpleEventHandler::class.java)
        eventHandler.onEvent(ChannelMessageEvent::class.java, ::handleChannelMessageEvent)
        eventHandler.onEvent(IRCMessageEvent::class.java, ::handleIRCMessage)

        client.pubSub.listenForCheerEvents(channelCredential.toCredential(), userId)
        eventHandler.onEvent(ChannelBitsEvent::class.java, ::handleBitsEvent)
    }

    private fun handleBitsEvent(request: ChannelBitsEvent) {
        channelBitsHandler.handleTwitchEvent(request)
    }

    private fun handleChannelMessageEvent(request: ChannelMessageEvent) {
        channelMessageHandler.handleTwitchEvent(request)
    }

    private fun handleIRCMessage(request: IRCMessageEvent) {
        ircMessageHandler.handleTwitchEvent(request)
    }

    private fun <T> TwitchIncomingEventHandler<T>.handleTwitchEvent(request: T) {
        this.handle(request, this@Channel, this@Channel.state).forEach { incomingEvent ->
            //TODO make suspendable
            runBlocking {
                eventHandlers.handleEvent(incomingEvent, this@Channel).execute()
            }
        }
    }

    private suspend fun List<OutgoingEvent>.execute() {
        coroutineScope {
            forEach { e ->
                @Suppress("IMPLICIT_CAST_TO_ANY")
                when (e) {
                    is TwitchOutgoingEvent -> {
                        try {
                            e.executeOnTwitch(bot.chat, chat, twitchApi, this@Channel)
                        } catch (e: Exception) {
                            LOGGER.error(e) { "Error while handling event ${e.message}" }
                        }
                    }
                    is Alert -> {
                        launch {
                            alerts.send(e)
                        }
                    }
                }.exhaustive()
            }
        }
    }

    suspend fun newAuth(auth: AuthToken) {
        LOGGER.info { "Save new credentials for channel : $name" }
        this.channelCredential.newAuth(auth)
    }

    private fun TwitchUserCredential.toCredential(): OAuth2Credential {
        return OAuth2Credential("twitch", this.authToken!!.access_token)
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}
