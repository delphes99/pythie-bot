package fr.delphes.features.twitch.command

import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.FieldDescription
import fr.delphes.annotation.dynamicForm.FieldMapper
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.connector.twitch.outgoingEvent.descriptionMapper.TwitchChannelMapper
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.state.state.TimeState
import fr.delphes.twitch.TwitchChannel
import java.time.Duration

@DynamicForm("twitch-command-form")
data class NewCommand(
    @FieldDescription("Channel where the command will be available")
    @FieldMapper(TwitchChannelMapper::class)
    override val channel: TwitchChannel,
    @FieldDescription("Command to trigger the response")
    val command: String,
    @FieldDescription("Cooldown")
    val cooldown: Duration = Duration.ZERO,
    @FieldDescription("Actions to do when the command is triggered")
    val actions: List<OutgoingEvent> = emptyList(),
) : TwitchFeature, FeatureDefinition {
    override val id: FeatureId = FeatureId()
    private val lastCallStateId = TimeState.id("cooldown-${id.value}")

    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime {
        return SimpleFeatureRuntime.whichHandle<CommandAsked>(id) {
            val lastCallState = stateManager.getState(lastCallStateId)
            if (event.data.command.triggerMessage == command && lastCallState.hasMoreThan(cooldown)) {
                actions.forEach { action ->
                    executeOutgoingEvent(action)
                }
                lastCallState.putNow()
            }
        }
    }

    override fun getSpecificStates(stateProvider: StateProvider): List<State> {
        return listOf(TimeState.withClockFrom(stateProvider, lastCallStateId.qualifier))
    }

    override val commands = listOf(Command(command))
}
