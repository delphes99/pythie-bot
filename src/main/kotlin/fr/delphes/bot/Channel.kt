package fr.delphes.bot

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.TwitchChat
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.chat.events.channel.IRCMessageEvent
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import fr.delphes.bot.command.Command
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.state.ChannelState
import fr.delphes.bot.state.CurrentStream
import fr.delphes.bot.util.time.toLocalDateTime
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.bot.twitch.game.Game
import fr.delphes.bot.twitch.game.TwitchGameRepository
import fr.delphes.bot.twitch.handler.ChannelMessageHandler
import fr.delphes.bot.twitch.handler.IRCMessageHandler
import fr.delphes.bot.twitch.handler.NewFollowHandler
import fr.delphes.bot.twitch.handler.NewSubHandler
import fr.delphes.bot.twitch.handler.RewardRedeemedHandler
import fr.delphes.bot.twitch.handler.StreamInfosHandler
import fr.delphes.bot.webserver.payload.newFollow.NewFollowPayload
import fr.delphes.bot.webserver.payload.newSub.NewSubPayload
import fr.delphes.bot.webserver.payload.streamInfos.StreamInfosPayload
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.feature.Feature
import mu.KotlinLogging
import java.time.LocalDateTime
import java.time.ZoneOffset

class Channel(
    configuration: ChannelConfiguration,
    val bot: ClientBot,
    private val state: ChannelState = ChannelState()
) : ChannelInfo {
    override val commands: List<Command> = configuration.features.flatMap(Feature::commands)
    override val currentStream: CurrentStream? get() = state.currentStream
    val name = configuration.ownerChannel
    val userId : String
    val oAuth = configuration.ownerAccountOauth

    val features = configuration.features
    private val ownerCredential = OAuth2Credential("twitch", "oauth:$oAuth")
    private val eventHandlers = EventHandlers()

    private val client: TwitchClient
    private val chat: TwitchChat

    private val newFollowHandler: TwitchIncomingEventHandler<NewFollowPayload> = NewFollowHandler()
    private val newSubHandler: TwitchIncomingEventHandler<NewSubPayload> = NewSubHandler()
    private val rewardRedeemedHandler: TwitchIncomingEventHandler<RewardRedeemedEvent> = RewardRedeemedHandler()
    private val channelMessageHandler: TwitchIncomingEventHandler<ChannelMessageEvent> = ChannelMessageHandler()
    private val ircMessageHandler: TwitchIncomingEventHandler<IRCMessageEvent> = IRCMessageHandler()
    private val streamInfosHandler: TwitchIncomingEventHandler<StreamInfosPayload> = StreamInfosHandler(TwitchGameRepository(this::getGame))

    private fun getGame(id: String) : Game {
        val game = client.helix.getGames(bot.appToken, listOf(id), null).execute().games.first()

        return Game(game.id, game.name)
    }

    private fun getStream(userId: String): CurrentStream? {
        return client.helix.getStreams(bot.appToken, null, null, 1, null, null, listOf(userId), null).execute()
            .streams
            .firstOrNull()
            ?.let {
                CurrentStream(it.title, LocalDateTime.ofInstant(it.startedAtInstant, ZoneOffset.UTC), getGame(it.gameId))
            }
    }

    init {
        this.userId = bot.client.helix.getUsers(null, null, listOf(name)).execute().users[0].id

        client = TwitchClientBuilder.builder()
            .withClientId(bot.clientId)
            .withClientSecret(bot.secretKey)
            .withEnablePubSub(true)
            .withEnableHelix(true)
            .withEnableChat(true)
            .withChatAccount(ownerCredential)
            .build()!!

        chat = client.chat
        LOGGER.debug { "Retrieved owner user id : $userId" }

        features.forEach { feature ->
            feature.registerHandlers(eventHandlers)
        }

        state.init(getStream(userId))

        chat.connect()
        client.pubSub.connect()

        //TODO subscribe only when feature requires
        val eventHandler = client.eventManager.getEventHandler(SimpleEventHandler::class.java)
        eventHandler.onEvent(ChannelMessageEvent::class.java, ::handleChannelMessageEvent)
        eventHandler.onEvent(RewardRedeemedEvent::class.java, ::handleRewardRedeemedEvent)
        eventHandler.onEvent(IRCMessageEvent::class.java, ::handleIRCMessage)

        client.pubSub.listenForChannelPointsRedemptionEvents(bot.botCredential, userId)
    }

    fun handleNewFollow(request: NewFollowPayload) {
        newFollowHandler.handleTwitchEvent(request)
    }

    fun handleNewSub(request: NewSubPayload) {
        newSubHandler.handleTwitchEvent(request)
    }

    fun handleStreamInfos(request: StreamInfosPayload) {
        streamInfosHandler.handleTwitchEvent(request)
    }

    private fun handleRewardRedeemedEvent(request: RewardRedeemedEvent) {
        rewardRedeemedHandler.handleTwitchEvent(request)
    }

    private fun handleChannelMessageEvent(request: ChannelMessageEvent) {
        channelMessageHandler.handleTwitchEvent(request)
    }

    private fun handleIRCMessage(request: IRCMessageEvent) {
        ircMessageHandler.handleTwitchEvent(request)
    }

    private fun <T> TwitchIncomingEventHandler<T>.handleTwitchEvent(request: T) {
        this.handle(request, this@Channel, this@Channel.state).forEach { incomingEvent ->
            eventHandlers.handleEvent(incomingEvent, this@Channel).execute()
        }
    }

    private fun List<OutgoingEvent>.execute() {
        forEach { e ->
            try {
                e.executeOnTwitch(bot.chat, chat, name)
            } catch (e: Exception) {
                LOGGER.error(e) { "Error while handling event ${e.message}" }
            }
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}
