package fr.delphes.features.twitch.command

import fr.delphes.bot.event.eventHandler.IncomingEventHandlerAction
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.features.twitch.handlersFor
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.state.state.TimeState
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid
import java.time.Duration

class CustomCommand(
    override val channel: TwitchChannel,
    val triggerCommand: Command,
    val cooldown: Duration = Duration.ZERO,
    override val id: FeatureId = FeatureId(),
    private val action: IncomingEventHandlerAction<CommandAsked>,
    //TODO merge TwitchFeature and FeatureDefinition
) : TwitchFeature, FeatureDefinition {
    constructor(
        channel: TwitchChannel,
        trigger: String,
        id: FeatureId = FeatureId(uuid()),
        cooldown: Duration = Duration.ZERO,
        action: IncomingEventHandlerAction<CommandAsked>,
    ) : this(channel, Command(trigger), cooldown, id, action)

    override val commands = setOf(triggerCommand)

    val lastCallStateId = TimeState.id("cooldown-${id.value}")

    override fun buildRuntime(stateManager: StateProvider): SimpleFeatureRuntime {
        val eventHandlers = channel.handlersFor<CommandAsked> {
            val lastCallState = stateManager.getState(lastCallStateId)
            if (event.data.command == triggerCommand && lastCallState.hasMoreThan(cooldown)) {
                lastCallState.putNow()

                action()
            }
        }

        return SimpleFeatureRuntime(eventHandlers, id)
    }

    override fun getSpecificStates(stateProvider: StateProvider): List<State> {
        return listOf(TimeState.withClockFrom(stateProvider, lastCallStateId.qualifier))
    }
}