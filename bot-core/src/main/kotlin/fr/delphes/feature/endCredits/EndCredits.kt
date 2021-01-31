package fr.delphes.feature.endCredits

import fr.delphes.bot.Channel
import fr.delphes.bot.command.Command
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.feature.Feature
import fr.delphes.feature.HaveHttp
import io.ktor.application.Application

class EndCredits : Feature, HaveHttp {
    override fun registerHandlers(eventHandlers: EventHandlers) {}

    override val module: (Channel) -> Application.() -> Unit = { channel ->
        EndCreditsModule(channel, channel.name)
    }
}