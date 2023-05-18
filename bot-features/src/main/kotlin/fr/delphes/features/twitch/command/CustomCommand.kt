package fr.delphes.features.twitch.command

import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.features.twitch.TwitchEventParameters
import fr.delphes.features.twitch.handlerFor
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.StateIdQualifier
import fr.delphes.state.StateManager
import fr.delphes.state.state.TimeState
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid
import java.time.Duration

class CustomCommand(
    override val channel: TwitchChannel,
    val triggerCommand: Command,
    val cooldown: Duration = Duration.ZERO,
    override val id: FeatureId = FeatureId(),
    private val action: suspend TwitchEventParameters<CommandAsked>.() -> Unit,
    //TODO merge TwitchFeature and FeatureDefinition
) : TwitchFeature, FeatureDefinition {
    constructor(
        channel: TwitchChannel,
        trigger: String,
        id: FeatureId = FeatureId(uuid()),
        cooldown: Duration = Duration.ZERO,
        action: suspend TwitchEventParameters<CommandAsked>.() -> Unit,
    ) : this(channel, Command(trigger), cooldown, id, action)

    override val commands = setOf(triggerCommand)

    val lastCallStateId = TimeState.id("cooldown-${id.value}")

    override fun buildRuntime(stateManager: StateManager): SimpleFeatureRuntime {
        val lastCallState =
            stateManager.getOrPut(lastCallStateId) { TimeState.withClockFrom(stateManager, StateIdQualifier(id.value)) }

        val eventHandlers = channel.handlerFor<CommandAsked> {
            //TODO use statemanager to retrieve state
            if (event.command == triggerCommand && lastCallState.hasMoreThan(cooldown)) {
                lastCallState.putNow()

                action()
            }
        }

        return SimpleFeatureRuntime(eventHandlers, id)
    }
}