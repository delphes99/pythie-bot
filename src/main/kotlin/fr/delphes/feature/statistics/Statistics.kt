package fr.delphes.feature.statistics

import fr.delphes.bot.Channel
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.feature.AbstractFeature
import fr.delphes.feature.HaveAdmin
import io.ktor.application.Application

class Statistics() : AbstractFeature(), HaveAdmin {
    override val module: (Channel) -> Application.() -> Unit = { channel ->
        StatisticsModule(channel, channel.name)
    }

    override fun registerHandlers(eventHandlers: EventHandlers) {}
}