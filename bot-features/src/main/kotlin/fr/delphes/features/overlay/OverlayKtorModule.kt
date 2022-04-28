package fr.delphes.features.overlay

import fr.delphes.bot.Bot
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.features.twitch.voth.VOTH
import fr.delphes.twitch.api.user.User
import fr.delphes.utils.time.prettyPrint
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.time.LocalDateTime

//TODO move to core
fun OverlayModule(
    bot: Bot
): Application.() -> Unit {
    //TODO no polling

    return {
        routing {
            val twitchConnector = bot.connectors.filterIsInstance<TwitchConnector>().first()
            twitchConnector.configuration?.listenedChannels?.forEach { channelConfiguration ->
                static("/${channelConfiguration.channel.normalizeName}/overlay") {
                    resources("overlay")
                }
                get("/${channelConfiguration.channel.normalizeName}/overlay/infos") {
                    val statistics = twitchConnector.statistics.of(channelConfiguration.channel).statistics

                    val overlayInfos = OverlayInfos(
                        last_follows = statistics.lastFollows.take(3).map(User::name),
                        last_subs = statistics.lastSubs.take(3).map(User::name),
                        last_cheers = statistics.lastCheers.take(3)
                            .map { cheer -> OverlayCheers(cheer.user?.name, cheer.bits) },
                        last_voths = lastVOTH(bot)
                    )

                    this@get.call.respond(overlayInfos)
                }
            }
        }
    }
}

//TODO voth inject informations
private fun lastVOTH(bot: Bot): List<OverlayVoth> {
    return bot
        .legacyfeatures
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