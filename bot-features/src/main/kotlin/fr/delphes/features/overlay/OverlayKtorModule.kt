package fr.delphes.features.overlay

import fr.delphes.bot.Bot
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.features.twitch.voth.VOTHState
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import fr.delphes.utils.time.prettyPrint
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

//TODO move to core
fun OverlayModule(
    bot: Bot,
): Application.() -> Unit {
    //TODO no polling

    return {
        routing {
            val twitchConnector = bot.connectors.filterIsInstance<TwitchConnector>().first()
            twitchConnector.configuration?.listenedChannels?.forEach { channelConfiguration ->
                get("/${channelConfiguration.channel.normalizeName}/overlay/infos") {
                    val statistics = twitchConnector.statistics.of(channelConfiguration.channel).statistics

                    val overlayInfos = OverlayInfos(
                        last_follows = statistics.lastFollows.take(3).map(UserName::name),
                        last_subs = statistics.lastSubs.take(3).map(UserName::name),
                        last_cheers = statistics.lastCheers.take(3)
                            .map { cheer -> OverlayCheers(cheer.user?.name, cheer.bits) },
                        last_voths = lastVOTH(bot, channelConfiguration.channel),
                    )

                    this@get.call.respond(overlayInfos)
                }
            }
        }
    }
}

//TODO voth inject informations
private fun lastVOTH(bot: Bot, channel: TwitchChannel): List<OverlayVoth> {
    return bot
        .stateManager
        .getState(VOTHState.idFor(channel))
        .lastReigns()
        .take(3)
        .map { voth -> OverlayVoth(voth.voth.name, voth.duration.prettyPrint()) }
}