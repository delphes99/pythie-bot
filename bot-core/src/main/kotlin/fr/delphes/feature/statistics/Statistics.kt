package fr.delphes.feature.statistics

import fr.delphes.bot.Channel
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.feature.Feature
import fr.delphes.feature.HaveHttp
import io.ktor.application.Application

class Statistics() : Feature, HaveHttp {
    override val module: (Channel) -> Application.() -> Unit = { channel ->
        StatisticsModule(channel, channel.name)
    }

    override fun registerHandlers(eventHandlers: EventHandlers) {}
}