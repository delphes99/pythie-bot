package fr.delphes.features.twitch.command

import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.rework.feature.CustomFeature
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.StateId
import fr.delphes.state.StateManager
import fr.delphes.state.TimeState
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid
import java.time.Duration

class CustomCommand(
    override val channel: TwitchChannel,
    trigger: String,
    override val id: FeatureId = FeatureId(uuid()),
    val cooldown: Duration = Duration.ZERO,
    private val action: suspend CustomCommandParameters.() -> Unit
) : CustomFeature, TwitchFeature {
    private val triggerCommand = Command(trigger)
    override val commands = setOf(triggerCommand)

    override fun buildRuntime(stateManager: StateManager): SimpleFeatureRuntime {
        val lastCallState = stateManager.getOrPut(StateId(id.value)) { TimeState(StateId(id.value), clock = stateManager.clock) }

        val eventHandlers = EventHandlers.of { event: CommandAsked, bot ->
            if (event.command == triggerCommand && event.channel == channel) {
                if(lastCallState.hasMoreThan(cooldown))  {
                    lastCallState.putNow()
                    CustomCommandParameters(bot, event).action()
                }
            }
        }

        return SimpleFeatureRuntime(eventHandlers, id)
    }
}