package fr.delphes.features.twitch.incomingRaid

import fr.delphes.bot.event.eventHandler.EventHandlerContext
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.IncomingRaid
import fr.delphes.features.twitch.handlersFor
import fr.delphes.features.twitch.streamerHighlight.StreamerHighlightState
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.state.state.ClockState
import fr.delphes.twitch.TwitchChannel

class IncomingRaidFeature(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(),
    val incomingRaidResponse: EventHandlerContext<IncomingRaid>,
) : TwitchFeature, FeatureDefinition {
    private val stateId = StreamerHighlightState.idFor(channel)

    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime {
        val eventHandlers = channel.handlersFor<IncomingRaid> {
            val user = event.leader
            val now = stateManager.getState(ClockState.ID).getValue()
            val highlightState = stateManager.getState(stateId)

            highlightState.highlight(user, now)
            incomingRaidResponse()
        }

        return SimpleFeatureRuntime(eventHandlers, id)
    }

    override fun getSpecificStates(stateProvider: StateProvider): List<State> {
        return listOf(
            StreamerHighlightState(channel)
        )
    }
}