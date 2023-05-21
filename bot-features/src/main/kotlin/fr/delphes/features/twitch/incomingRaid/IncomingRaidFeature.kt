package fr.delphes.features.twitch.incomingRaid

import fr.delphes.bot.event.eventHandler.EventHandlerContext
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.IncomingRaid
import fr.delphes.features.twitch.handlerFor
import fr.delphes.features.twitch.streamerHighlight.StreamerHighlightState
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.StateManager
import fr.delphes.state.state.ClockState
import fr.delphes.twitch.TwitchChannel

class IncomingRaidFeature(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(),
    val incomingRaidResponse: EventHandlerContext<IncomingRaid>,
) : TwitchFeature, FeatureDefinition {
    override fun buildRuntime(stateManager: StateManager): FeatureRuntime {
        val eventHandlers = channel.handlerFor<IncomingRaid> {
            val user = event.leader
            val now = stateManager.getStateOrNull(ClockState.ID)?.getValue() ?: return@handlerFor
            val highlightState = stateManager.getOrPut(StreamerHighlightState.idFor(channel)) {
                StreamerHighlightState(channel)
            }

            highlightState.highlight(user, now)
            incomingRaidResponse()
        }

        return SimpleFeatureRuntime(eventHandlers, id)
    }
}