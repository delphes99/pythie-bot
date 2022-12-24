package fr.delphes.features.twitch.command

import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.features.twitch.TwitchEventParameters
import fr.delphes.features.twitch.handlerFor
import fr.delphes.rework.feature.CustomFeature
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.StateId
import fr.delphes.state.StateManager
import fr.delphes.state.state.TimeState
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid
import java.time.Duration

class CustomCommand(
    override val channel: TwitchChannel,
    trigger: String,
    override val id: FeatureId = FeatureId(uuid()),
    val cooldown: Duration = Duration.ZERO,
    private val action: suspend TwitchEventParameters<CommandAsked>.() -> Unit
) : CustomFeature, TwitchFeature {
    private val triggerCommand = Command(trigger)
    override val commands = setOf(triggerCommand)

    override fun buildRuntime(stateManager: StateManager): SimpleFeatureRuntime {
        val lastCallState = stateManager.getOrPut(StateId(id.value)) { TimeState.withClockFrom(stateManager, StateId(id.value)) }

        val eventHandlers = channel.handlerFor<CommandAsked> {
            if (event.command == triggerCommand && lastCallState.hasMoreThan(cooldown)) {
                lastCallState.putNow()

                action()
            }
        }

        return SimpleFeatureRuntime(eventHandlers, id)
    }
}