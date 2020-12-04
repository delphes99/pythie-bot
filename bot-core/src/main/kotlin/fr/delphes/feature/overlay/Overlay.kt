package fr.delphes.feature.overlay

import fr.delphes.bot.Channel
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.command.Command
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.BitCheered
import fr.delphes.bot.event.incoming.NewFollow
import fr.delphes.bot.event.incoming.NewSub
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.Feature
import fr.delphes.feature.HaveHttp
import io.ktor.application.Application

class Overlay : Feature, HaveHttp {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(NewFollowHandler())
        eventHandlers.addHandler(NewSubHandler())
        eventHandlers.addHandler(BitCheeredHandler())
    }

    override val commands: Iterable<Command> = emptyList()
    override val module: (Channel) -> Application.() -> Unit = { channel ->
        OverlayModule(channel)
    }

    inner class NewFollowHandler : EventHandler<NewFollow> {
        override suspend fun handle(event: NewFollow, channel: ChannelInfo): List<OutgoingEvent> =
            listOf(
                Alert(
                    "newFollow",
                    "newFollow" to event.follower.name
                )
            )
    }

    inner class NewSubHandler : EventHandler<NewSub> {
        override suspend fun handle(event: NewSub, channel: ChannelInfo): List<OutgoingEvent> =
            listOf(
                Alert(
                    "newSub",
                    "newSub" to event.sub.name
                )
            )
    }

    inner class BitCheeredHandler : EventHandler<BitCheered> {
        override suspend fun handle(event: BitCheered, channel: ChannelInfo): List<OutgoingEvent> =
            listOf(
                Alert(
                    "newCheer",
                    "cheerer" to (event.cheerer?.name ?: "")
                )
            )
    }
}