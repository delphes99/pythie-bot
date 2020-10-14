package fr.delphes.bot

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.TwitchChat
import fr.delphes.bot.command.Command
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.MessageReceived
import fr.delphes.bot.event.incoming.NewFollow
import fr.delphes.bot.event.incoming.NewSub
import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.bot.event.incoming.StreamOffline
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.event.incoming.VIPListReceived
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.webserver.payload.newFollow.NewFollowPayload
import fr.delphes.bot.webserver.payload.newSub.NewSubPayload
import fr.delphes.bot.webserver.payload.streamInfos.StreamInfosPayload
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.feature.Feature
import io.ktor.request.ApplicationRequest
import io.ktor.request.receive
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

class Channel(
    configuration: ChannelConfiguration,
    val bot: ClientBot
) {
    val commands: List<Command> = configuration.features.flatMap(Feature::commands)
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
            .withEnableChat(true)
            .withChatAccount(ownerCredential)
            .build()!!

        chat = client.chat
        LOGGER.debug { "Retrieved owner user id : $userId" }

        features.forEach { feature ->
            feature.registerHandlers(eventHandlers)
        }
    }

    fun handleNewFollow(request: ApplicationRequest) {
        val payload = runBlocking {
            request.call.receive<NewFollowPayload>()
        }
        payload.data.forEach { newFollowPayload ->
            eventHandlers.handleEvent(NewFollow(newFollowPayload)).execute()
        }
    }

    fun handleNewSub(request: ApplicationRequest) {
        val payload = runBlocking {
            request.call.receive<NewSubPayload>()
        }
        payload.data.forEach { newSubPayload ->
            eventHandlers.handleEvent(NewSub(newSubPayload)).execute()
        }
    }

    fun handleStreamInfos(request: ApplicationRequest) {
        val payload = runBlocking {
            request.call.receive<StreamInfosPayload>()
        }
        val streamInfos = payload.data
        if (streamInfos.isEmpty()) {
            eventHandlers.handleEvent(StreamOffline()).execute()
        } else {
            streamInfos.forEach { streamInfosData ->
                eventHandlers.handleEvent(StreamOnline()).execute()
            }
        }
    }

    fun handleVIPListReceived(event: VIPListReceived) {
        eventHandlers.handleEvent(event).execute()
    }

    fun handleRewardRedeemedEvent(rewardRedemption: RewardRedemption) {
        eventHandlers.handleEvent(rewardRedemption).execute()
    }

    fun handleChannelMessage(messageReceived: MessageReceived) {
        eventHandlers.handleEvent(messageReceived).execute()
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
