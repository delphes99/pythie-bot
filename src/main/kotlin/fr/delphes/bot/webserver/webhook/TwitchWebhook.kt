package fr.delphes.bot.webserver.webhook

import com.github.twitch4j.helix.webhooks.topics.TwitchWebhookTopic
import fr.delphes.bot.Bot
import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

enum class TwitchWebhook(
    private val topicUrl: (OwnerId) -> String,
    val callSuffix: String,
    val notificationHandler: Bot.(PipelineContext<Unit, ApplicationCall>) -> Unit
) {
    FOLLOW(
        { userId -> "https://api.twitch.tv/helix/users/follows?first=1&to_id=$userId" },
        "follow",
        { context -> handleNewFollow(context.context.request) }
    ),
    STREAM(
        { userId -> "https://api.twitch.tv/helix/streams?user_id=$userId" },
        "streamstatus",
        { context -> handleStreamInfos(context.context.request) }
    );

    fun topic(ownerId: OwnerId) = TwitchWebhookTopic.fromUrl(topicUrl(ownerId))

    companion object : Iterable<TwitchWebhook> by values().toList()
}

typealias OwnerId = String