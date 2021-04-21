package fr.delphes.features.twitch.statistics

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.feature.HaveHttp
import fr.delphes.feature.NonEditableFeature
import fr.delphes.twitch.TwitchChannel
import io.ktor.application.Application

class Statistics(
    private val channel: TwitchChannel
) : NonEditableFeature<StatisticsDescription>, HaveHttp {
    override fun description() = StatisticsDescription()

    override val module: (Bot) -> Application.() -> Unit = {
        val twitchConnector = it.connectors.filterIsInstance<TwitchConnector>().first()
        StatisticsModule(channel, twitchConnector)
    }

    override val eventHandlers = EventHandlers()
}