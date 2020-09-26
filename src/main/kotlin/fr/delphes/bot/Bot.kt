package fr.delphes.bot

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.TwitchChat
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.chat.events.channel.IRCMessageEvent
import com.github.twitch4j.helix.webhooks.domain.WebhookRequest
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import fr.delphes.User
import fr.delphes.VIPParser
import fr.delphes.bot.webserver.payload.newFollow.NewFollowPayload
import fr.delphes.bot.webserver.webhook.TwitchWebhook
import fr.delphes.configuration.Configuration
import fr.delphes.event.eventHandler.EventHandler
import fr.delphes.event.eventHandler.handleEvent
import fr.delphes.event.incoming.IncomingEvent
import fr.delphes.event.incoming.MessageReceived
import fr.delphes.event.incoming.NewFollow
import fr.delphes.event.incoming.RewardRedemption
import fr.delphes.event.incoming.VIPListReceived
import fr.delphes.feature.Feature
import fr.delphes.storage.serialization.Serializer
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.ApplicationRequest
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.runBlocking

data class Bot(
    val channel: String,
    val features: List<Feature>,
    val twitchClient: TwitchClient,
    val ownerTwitchClient: TwitchClient
) {
    init {
        val eventHandler = twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java)
        eventHandler.onEvent(ChannelMessageEvent::class.java, ::handleChannelMessage)
        eventHandler.onEvent(RewardRedeemedEvent::class.java, ::handleRewardRedeemedEvent)
        eventHandler.onEvent(IRCMessageEvent::class.java, ::handleIRCMessage)

        embeddedServer(Netty, 80) {
            install(ContentNegotiation) {
                json(
                    json = Serializer
                )
            }
            //TODO verify secret send on subscription
            //TODO manage duplicate event
            routing {
                TwitchWebhook.forEach { webhook ->
                    get("/${webhook.callSuffix}") {
                        challengeWebHook()
                    }
                    post("/${webhook.callSuffix}") {
                        webhook.notificationHandler(this@Bot ,this)
                        this.context.response.status(HttpStatusCode.OK)
                    }
                }
            }
        }.start(wait = false)
    }

    private suspend fun PipelineContext<Unit, ApplicationCall>.challengeWebHook() {
        call.respondText(
            this.context.parameters["hub.challenge"] ?: "No challenge provided",
            ContentType.Text.Html
        )
    }

    companion object {
        //private val WEBHOOK_DURATION = Duration.ofDays(1).toSeconds().toInt()
        private val WEBHOOK_DURATION = 60

        fun build(
            configuration: Configuration,
            features: List<Feature>
        ): Bot {
            val botCredential = OAuth2Credential("twitch", "oauth:${configuration.botAccountOauth}")
            val ownerCredential = OAuth2Credential("twitch", "oauth:${configuration.ownerAccountOauth}")

            val baseClientBuilder = TwitchClientBuilder.builder()
                .withClientId(configuration.clientId)
                .withClientSecret(configuration.secretKey)

            val twitchClient = baseClientBuilder
                .withEnablePubSub(true)
                .withEnableHelix(true)
                .withEnableChat(true)
                .withChatAccount(botCredential)
                .build()

            val ownerTwitchClient = baseClientBuilder
                .withEnableChat(true)
                .withChatAccount(ownerCredential)
                .build()

            val chat = twitchClient.chat

            chat.connect()
            chat.joinChannel(configuration.ownerChannel);

            twitchClient.pubSub.connect()
            twitchClient.pubSub.listenForChannelPointsRedemptionEvents(botCredential, configuration.ownerChannelId)

            val userId =
                twitchClient.helix.getUsers(null, null, listOf(configuration.ownerChannel)).execute().users[0].id
            val tunnel = Ngrok.createHttpTunnel(80, "bot")

            //TODO cron refresh sub
            TwitchWebhook.forEach { twitchWebhook ->
                twitchClient.helix.requestWebhookSubscription(
                    WebhookRequest(
                        "${tunnel.publicUrl}/${twitchWebhook.callSuffix}",
                        "subscribe",
                        twitchWebhook.topic(userId),
                        WEBHOOK_DURATION,
                        "toto"
                    ),
                    configuration
                        .ownerAccountOauth
                ).execute()
            }

            return Bot(
                configuration.ownerChannel,
                features,
                twitchClient,
                ownerTwitchClient
            )
        }
    }

    private val chat: TwitchChat get() = twitchClient.chat
    private val ownerChat: TwitchChat get() = ownerTwitchClient.chat

    private val rewardRedeptionHandlers = features.flatMap(Feature::rewardHandlers)
    private val vipListReceivedHandlers = features.flatMap(Feature::vipListReceivedHandlers)
    private val messageReceivedHandlers = features.flatMap(Feature::messageReceivedHandlers)
    private val newFollowHandlers = features.flatMap(Feature::newFollowHandlers)

    private fun handleRewardRedeemedEvent(event: RewardRedeemedEvent) {
        rewardRedeptionHandlers.handleEventAndApply(RewardRedemption(event))
    }

    private fun handleIRCMessage(event: IRCMessageEvent) {
        event.message?.also {
            if (it.isPresent) {
                val vipResult = VIPParser.extractVips(it.get())
                if (vipResult is VIPParser.VIPResult.VIPList) {
                    val vipListReceived = VIPListReceived(vipResult.users)

                    vipListReceivedHandlers.handleEventAndApply(vipListReceived)
                }
            }
        }
    }

    private fun handleChannelMessage(event: ChannelMessageEvent) {
        messageReceivedHandlers.handleEventAndApply(MessageReceived(event))
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
        //TODO handle stream on / stream off
        /*val payload = runBlocking {
            request.call.receive<NewFollowPayload>()
        }
        payload.data.forEach { newFollowPayload ->
            newFollowHandlers.handleEventAndApply(NewFollow(User(newFollowPayload)))
        }*/
    }

    private fun <T : IncomingEvent> List<EventHandler<T>>.handleEventAndApply(event: T) {
        val outgoingEvents = this.handleEvent(event)

        outgoingEvents.forEach { e ->
            e.applyOnTwitch(chat, ownerChat, channel)
        }
    }
}