package fr.delphes.feature.overlay

import fr.delphes.User
import fr.delphes.bot.Channel
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

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
                    OverlayInfos(statistics.lastFollows.map(User::name))
                } else {
                    OverlayInfos(emptyList())
                }
                this.call.respond(overlayInfos)
            }
        }
    }
}