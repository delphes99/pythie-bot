package fr.delphes.features.twitch.endCredits

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.feature.HaveHttp
import fr.delphes.feature.NonEditableFeature
import io.ktor.server.application.Application

class EndCredits : NonEditableFeature<EndCreditsDescription>, HaveHttp {
    override fun description() = EndCreditsDescription()

    override val eventHandlers = EventHandlers.Empty

    override val module: (Bot) -> Application.() -> Unit = {
        val twitchConnector = it.connectors.filterIsInstance<TwitchConnector>().first()
        EndCreditsModule(twitchConnector)
    }
}