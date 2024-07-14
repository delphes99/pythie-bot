package fr.delphes.features.twitch.newFollow

import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.FieldDescription
import fr.delphes.annotation.dynamicForm.FieldMapper
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.incomingEvent.NewFollow
import fr.delphes.connector.twitch.outgoingEvent.descriptionMapper.TwitchChannelMapper
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel
import java.time.Duration

@DynamicForm("twitch-new-follow-form")
data class NewFollow(
    @FieldMapper(TwitchChannelMapper::class)
    @FieldDescription("Channel where the follow will be detected")
    val channel: TwitchChannel,
    @FieldDescription("Cooldown between two follow detection")
    val cooldown: Duration,
    @FieldDescription("Actions to do when a new follow is detected")
    val actions: List<OutgoingEvent> = emptyList(),
) : FeatureDefinition {
    override val id: FeatureId = FeatureId()
    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime {
        return SimpleFeatureRuntime.whichHandle<NewFollow>(id) {
            //TODO launch actions
        }
    }

    override fun getSpecificStates(stateProvider: StateProvider): List<State> {
        return emptyList()
    }
}