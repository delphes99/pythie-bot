package fr.delphes.feature.overlay

import fr.delphes.twitch.api.user.User
import fr.delphes.bot.Channel
import fr.delphes.feature.voth.VOTH
import fr.delphes.feature.voth.VOTHReign
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import java.time.LocalDateTime

fun OverlayModule(
    channel: Channel
): Application.() -> Unit {
    val channelName = channel.name

    return {
        routing {
            static("/${channelName}/overlay") {
                resources("overlay")
            }
            get("/${channelName}/overlay/infos") {
                val statistics = channel.statistics
                val overlayInfos = if(statistics != null) {
                    OverlayInfos(statistics.lastFollows.take(3).map(User::name), lastVOTH(channel))
                } else {
                    OverlayInfos(emptyList(), emptyList())
                }
                this.call.respond(overlayInfos)
            }
        }
    }
}

private fun lastVOTH(channel: Channel): List<String> {
    return channel.features
        .filterIsInstance<VOTH>()
        .firstOrNull()
        .let { voth ->
            voth
                ?.state
                ?.lastReigns(LocalDateTime.now())
                ?.take(3)
                ?.map(VOTHReign::voth)
                ?.map(User::name)
        } ?: emptyList()
}