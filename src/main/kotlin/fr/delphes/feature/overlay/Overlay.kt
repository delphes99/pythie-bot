package fr.delphes.feature.overlay

import fr.delphes.bot.Channel
import fr.delphes.bot.command.Command
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.feature.Feature
import fr.delphes.feature.HaveHttp
import io.ktor.application.Application

class Overlay : Feature, HaveHttp {
    override fun registerHandlers(eventHandlers: EventHandlers) {}

    override val commands: Iterable<Command> = emptyList()
    override val module: (Channel) -> Application.() -> Unit = { channel ->
        OverlayModule(channel)
    }
}