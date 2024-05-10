package fr.delphes.features.twitch.command

import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.FieldDescription
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider

@DynamicForm("twitch-command-form")
data class NewCommand(
    @FieldDescription("Channel where the command will be available")
    val channel: String,
    @FieldDescription("Command to trigger the response")
    val command: String,
    @FieldDescription("Actions to do when the command is triggered")
    val actions: List<OutgoingEvent> = emptyList(),
) : FeatureDefinition {
    override val id: FeatureId = FeatureId()
    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime {
        TODO("Not yet implemented")
    }

    override fun getSpecificStates(stateProvider: StateProvider): List<State> {
        TODO("Not yet implemented")
    }
}
