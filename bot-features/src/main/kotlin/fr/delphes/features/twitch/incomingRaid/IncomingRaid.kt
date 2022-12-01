package fr.delphes.features.twitch.incomingRaid

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.IncomingRaid
import fr.delphes.feature.NonEditableFeature
import fr.delphes.feature.StateManagerWithRepository
import fr.delphes.feature.WithStateManager
import fr.delphes.features.twitch.streamerHighlight.StreamerHighlightState
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock

class IncomingRaid(
    override val channel: TwitchChannel,
    stateManagerWithRepository: StateManagerWithRepository<StreamerHighlightState>,
    val incomingRaidResponse: (IncomingRaid) -> List<OutgoingEvent>,
    private val clock: Clock = SystemClock,
) : NonEditableFeature,
    TwitchFeature,
    WithStateManager<StreamerHighlightState> by stateManagerWithRepository {
    override val eventHandlers = LegacyEventHandlers
            .builder()
            .addHandler(IncomingRaidHandler())
            .build()

    private inner class IncomingRaidHandler : TwitchEventHandler<IncomingRaid>(channel) {
        override suspend fun handleIfGoodChannel(event: IncomingRaid, bot: Bot): List<OutgoingEvent> {
            newState(state.highlight(event.leader, clock.now()))
            return incomingRaidResponse(event)
        }
    }
}