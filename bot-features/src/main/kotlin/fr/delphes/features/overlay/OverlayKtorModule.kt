package fr.delphes.features.overlay

import fr.delphes.bot.Bot
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.features.twitch.voth.VOTH
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.utils.time.prettyPrint
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import java.time.LocalDateTime

//TODO move to core
fun OverlayModule(
    bot: Bot
): Application.() -> Unit {
    //TODO no polling

    return {
        routing {
            val twitchConnector = bot.connectors.filterIsInstance<TwitchConnector>().first()
            twitchConnector.configuration.listenedChannels.forEach { channelConfiguration ->
                //TODO normalize twitch channel name
                static("/${channelConfiguration.channel.name.toLowerCase()}/overlay") {
                    resources("overlay")
                }
                //TODO normalize twitch channel name
                get("/${channelConfiguration.channel.name.toLowerCase()}/overlay/infos") {
                    twitchConnector.whenRunning(
                        whenRunning = {
                            val channel = this.clientBot.channelOf(TwitchChannel(channelConfiguration.channel.name))!!
                            val statistics = channel.statistics
                            val overlayInfos = OverlayInfos(
                                last_follows = statistics.lastFollows.take(3).map(User::name),
                                last_subs = statistics.lastSubs.take(3).map(User::name),
                                last_cheers = statistics.lastCheers.take(3)
                                    .map { cheer -> OverlayCheers(cheer.user?.name, cheer.bits) },
                                last_voths = lastVOTH(bot)
                            )

                            this@get.call.respond(overlayInfos)
                        },
                        whenNotRunning = {
                            this@get.call.respond(HttpStatusCode.NoContent)
                        }
                    )
                }
            }
        }
    }
}

//TODO voth inject informations
private fun lastVOTH(bot: Bot): List<OverlayVoth> {
    return bot
        .features
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