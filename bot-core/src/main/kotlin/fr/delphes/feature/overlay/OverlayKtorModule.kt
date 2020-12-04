package fr.delphes.feature.overlay

import fr.delphes.bot.Channel
import fr.delphes.bot.util.time.prettyPrint
import fr.delphes.feature.voth.VOTH
import fr.delphes.twitch.api.user.User
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

    //TODO no polling

    return {
        routing {
            static("/${channelName}/overlay") {
                resources("overlay")
            }
            get("/${channelName}/overlay/infos") {
                val statistics = channel.statistics
                val overlayInfos = OverlayInfos(
                    last_follows = statistics.lastFollows.take(3).map(User::name),
                    last_subs = statistics.lastSubs.take(3).map(User::name),
                    last_cheers = statistics.lastCheers.take(3).map { cheer -> OverlayCheers(cheer.user?.name, cheer.bits) },
                    last_voths = lastVOTH(channel)
                )
                this.call.respond(overlayInfos)
            }
        }
    }
}

private fun lastVOTH(channel: Channel): List<OverlayVoth> {
    return channel.features
        .filterIsInstance<VOTH>()
        .firstOrNull()
        .let { vothFeature ->
            vothFeature
                ?.state
                ?.lastReigns(LocalDateTime.now())
                ?.take(3)
                ?.map { voth -> OverlayVoth(voth.voth.name, voth.duration.prettyPrint()) }
        } ?: emptyList()
}