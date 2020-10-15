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
import fr.delphes.bot.twitch.adapter.ChannelMessageHandler
import fr.delphes.bot.twitch.adapter.IRCMessageHandler
import fr.delphes.bot.twitch.adapter.NewFollowHandler
import fr.delphes.bot.twitch.adapter.NewSubHandler
import fr.delphes.bot.twitch.adapter.RewardRedeemedHandler
import fr.delphes.bot.twitch.adapter.StreamInfosHandler
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.feature.Feature
import io.ktor.request.ApplicationRequest
import mu.KotlinLogging

class Channel(
    configuration: ChannelConfiguration,
    val bot: ClientBot
) : ChannelInfo {
    override val commands: List<Command> = configuration.features.flatMap(Feature::commands)
    val name = configuration.ownerChannel
    val userId : String
    val oAuth = configuration.ownerAccountOauth
    val features = configuration.features
    private val ownerCredential = OAuth2Credential("twitch", "oauth:$oAuth")
    private val eventHandlers = EventHandlers()

    private val client: TwitchClient
    private val chat: TwitchChat

    init {
        this.userId = bot.client.helix.getUsers(null, null, listOf(name)).execute().users[0].id

        client = TwitchClientBuilder.builder()
            .withClientId(bot.clientId)
            .withClientSecret(bot.secretKey)
            .withEnablePubSub(true)
            .withEnableChat(true)
            .withChatAccount(ownerCredential)
            .build()!!

        chat = client.chat
        LOGGER.debug { "Retrieved owner user id : $userId" }

        features.forEach { feature ->
            feature.registerHandlers(eventHandlers)
        }

        chat.connect()
        client.pubSub.connect()

        //TODO subscribe only when feature requires
        val eventHandler = client.eventManager.getEventHandler(SimpleEventHandler::class.java)
        eventHandler.onEvent(ChannelMessageEvent::class.java, ::handleChannelMessageEvent)
        eventHandler.onEvent(RewardRedeemedEvent::class.java, ::handleRewardRedeemedEvent)
        eventHandler.onEvent(IRCMessageEvent::class.java, ::handleIRCMessage)

        client.pubSub.listenForChannelPointsRedemptionEvents(bot.botCredential, userId)
    }

    fun handleNewFollow(request: ApplicationRequest) {
        NewFollowHandler().transform(request).forEach { newFollow ->
            eventHandlers.handleEvent(newFollow, this).execute()
        }
    }

    fun handleNewSub(request: ApplicationRequest) {
        NewSubHandler().transform(request).forEach { newFollow ->
            eventHandlers.handleEvent(newFollow, this).execute()
        }
    }

    fun handleStreamInfos(request: ApplicationRequest) {
        StreamInfosHandler().transform(request).forEach { event ->
            eventHandlers.handleEvent(event, this).execute()
        }
    }

    private fun handleRewardRedeemedEvent(event: RewardRedeemedEvent) {
        RewardRedeemedHandler().transform(event).forEach { event ->
            eventHandlers.handleEvent(event, this).execute()
        }
    }

    private fun handleChannelMessageEvent(channelMessageEvent: ChannelMessageEvent) {
        ChannelMessageHandler(this).transform(channelMessageEvent).forEach { event ->
            eventHandlers.handleEvent(event, this).execute()
        }
    }

    private fun handleIRCMessage(event: IRCMessageEvent) {
        IRCMessageHandler().transform(event).forEach { event ->
            eventHandlers.handleEvent(event, this).execute()
        }
    }

    fun executeEvents(outgoingEvents: List<OutgoingEvent>) {
        outgoingEvents.execute()
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
