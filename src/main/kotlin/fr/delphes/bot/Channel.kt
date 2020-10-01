package fr.delphes.bot

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.TwitchChat
import fr.delphes.bot.webserver.payload.newFollow.NewFollowPayload
import fr.delphes.bot.webserver.payload.streamInfos.StreamInfosPayload
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.event.eventHandler.EventHandler
import fr.delphes.event.eventHandler.handleEvent
import fr.delphes.event.incoming.IncomingEvent
import fr.delphes.event.incoming.MessageReceived
import fr.delphes.event.incoming.NewFollow
import fr.delphes.event.incoming.RewardRedemption
import fr.delphes.event.incoming.StreamOffline
import fr.delphes.event.incoming.StreamOnline
import fr.delphes.event.incoming.VIPListReceived
import fr.delphes.event.outgoing.OutgoingEvent
import fr.delphes.feature.Feature
import io.ktor.request.ApplicationRequest
import io.ktor.request.receive
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

class Channel(
    configuration: ChannelConfiguration,
    val bot: ClientBot
) {
    val name = configuration.ownerChannel
    val userId : String
    val oAuth = configuration.ownerAccountOauth
    private val features = configuration.features
    private val ownerCredential = OAuth2Credential("twitch", "oauth:$oAuth")

    private val messageReceivedHandlers = features.flatMap(Feature::messageReceivedHandlers)
    private val rewardRedeptionHandlers = features.flatMap(Feature::rewardHandlers)
    private val vipListReceivedHandlers = features.flatMap(Feature::vipListReceivedHandlers)
    private val newFollowHandlers = features.flatMap(Feature::newFollowHandlers)
    private val streamOfflineHandlers = features.flatMap(Feature::streamOfflineHandlers)
    private val streamOnlineHandlers = features.flatMap(Feature::streamOnlineHandlers)

    val client: TwitchClient

    private val chat: TwitchChat

    init {
        this.userId = bot.client.helix.getUsers(null, null, listOf(name)).execute().users[0].id

        client = TwitchClientBuilder.builder()
            .withClientId(userId)
            .withClientSecret(bot.secretKey)
            .withEnableChat(true)
            .withChatAccount(ownerCredential)
            .build()!!

        chat = client.chat
        LOGGER.debug { "Retrieved owner user id : $userId" }
    }

    fun handleNewFollow(request: ApplicationRequest) {
        val payload = runBlocking {
            request.call.receive<NewFollowPayload>()
        }
        payload.data.forEach { newFollowPayload ->
            newFollowHandlers.handleEventAndApply(NewFollow(newFollowPayload))
        }
    }

    fun handleStreamInfos(request: ApplicationRequest) {
        val payload = runBlocking {
            request.call.receive<StreamInfosPayload>()
        }
        val streamInfos = payload.data
        if (streamInfos.isEmpty()) {
            streamOfflineHandlers.handleEventAndApply(StreamOffline())
        } else {
            streamInfos.forEach { streamInfosData ->
                streamOnlineHandlers.handleEventAndApply(StreamOnline())
            }
        }
    }

    fun handleVIPListReceived(event: VIPListReceived) {
        vipListReceivedHandlers.handleEventAndApply(event)
    }

    fun handleRewardRedeemedEvent(rewardRedemption: RewardRedemption) {
        rewardRedeptionHandlers.handleEventAndApply(rewardRedemption)
    }

    fun handleChannelMessage(messageReceived: MessageReceived) {
        messageReceivedHandlers.handleEventAndApply(messageReceived)
    }

    private fun <T : IncomingEvent> List<EventHandler<T>>.handleEventAndApply(event: T) {
        LOGGER.debug { "Handle event : ${event.javaClass} \n $event" }
        val outgoingEvents = this.handleEvent(event)

        outgoingEvents.apply()
    }

    private fun List<OutgoingEvent>.apply() {
        forEach { e ->
            try {
                e.applyOnTwitch(bot.chat, chat, name)
            } catch (e: Exception) {
                LOGGER.error(e) { "Error while handling event ${e.message}" }
            }
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}
