package fr.delphes.features.overlay

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.connector.twitch.incomingEvent.NewFollow
import fr.delphes.connector.twitch.incomingEvent.NewSub
import fr.delphes.feature.Feature
import fr.delphes.feature.HaveHttp
import fr.delphes.twitch.TwitchChannel
import io.ktor.application.Application

class Overlay(private val channel: TwitchChannel) : Feature, HaveHttp {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(NewFollowHandler())
        eventHandlers.addHandler(NewSubHandler())
        eventHandlers.addHandler(BitCheeredHandler())
    }

    override val module: (Bot) -> Application.() -> Unit = {
        OverlayModule(it)
    }

    inner class NewFollowHandler : TwitchEventHandler<NewFollow>(channel) {
        override suspend fun handleIfGoodChannel(event: NewFollow, bot: Bot): List<OutgoingEvent> =
            listOf(
                Alert(
                    "newFollow",
                    "newFollow" to event.follower.name
                )
            )
    }

    inner class NewSubHandler : TwitchEventHandler<NewSub>(channel) {
        override suspend fun handleIfGoodChannel(event: NewSub, bot: Bot): List<OutgoingEvent> =
            listOf(
                Alert(
                    "newSub",
                    "newSub" to event.sub.name
                )
            )
    }

    inner class BitCheeredHandler : TwitchEventHandler<BitCheered>(channel) {
        override suspend fun handleIfGoodChannel(event: BitCheered, bot: Bot): List<OutgoingEvent> =
            listOf(
                Alert(
                    "newCheer",
                    "cheerer" to (event.cheerer?.name ?: "")
                )
            )
    }
}