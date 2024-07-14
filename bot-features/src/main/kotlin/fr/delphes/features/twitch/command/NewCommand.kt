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
import fr.delphes.twitch.TwitchChannel

@DynamicForm("twitch-command-form")
data class NewCommand(
    @FieldDescription("Channel where the command will be available")
    @FieldMapper(TwitchChannelMapper::class)
    override val channel: TwitchChannel,
    @FieldDescription("Command to trigger the response")
    val command: String,
    @FieldDescription("Actions to do when the command is triggered")
    val actions: List<OutgoingEvent> = emptyList(),
) : TwitchFeature, FeatureDefinition {
    override val id: FeatureId = FeatureId()
    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime {
        return SimpleFeatureRuntime.whichHandle<CommandAsked>(id) {
            if (event.data.command.triggerMessage == command) {
                //TODO retrieve action from data
            }
        }
    }

    override fun getSpecificStates(stateProvider: StateProvider): List<State> {
        return emptyList()
    }

    override val commands = listOf(Command(command))
}
