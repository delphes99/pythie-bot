package fr.delphes.bot.webserver.webhook

import com.github.twitch4j.helix.webhooks.topics.TwitchWebhookTopic
import fr.delphes.bot.Channel
import io.ktor.application.ApplicationCall
import io.ktor.request.receive
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.runBlocking

enum class TwitchWebhook(
    private val topicUrl: (OwnerId) -> String,
    val callSuffix: String,
    val notificationHandler: Channel.(PipelineContext<Unit, ApplicationCall>) -> Unit
) {
    FOLLOW(
        { userId -> "https://api.twitch.tv/helix/users/follows?first=1&to_id=$userId" },
        "follow",
        { context -> handleNewFollow(context.payload()) }
    ),
    SUB(
        { userId -> "https://api.twitch.tv/helix/subscriptions/events?broadcaster_id=$userId&first=1" },
        "sub",
        { context -> handleNewSub(context.payload()) }
    ),
    STREAM(
        { userId -> "https://api.twitch.tv/helix/streams?user_id=$userId" },
        "streamstatus",
        { context -> handleStreamInfos(context.payload()) }
    );

    fun topic(ownerId: OwnerId) = TwitchWebhookTopic.fromUrl(topicUrl(ownerId))

    companion object : Iterable<TwitchWebhook> by values().toList()
}

private inline fun <reified T: Any> PipelineContext<Unit, ApplicationCall>.payload(): T {
    return runBlocking {
        context.request.call.receive()
    }
}

typealias OwnerId = String