package fr.delphes.features.obs

import fr.delphes.bot.event.eventHandler.IncomingEventHandlerAction
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.connector.obs.incomingEvent.SourceFilterActivated as ObsSourceFilterActivated

class SourceFilterActivatedFeature(
    override val id: FeatureId = FeatureId(),
    val action: IncomingEventHandlerAction<ObsSourceFilterActivated>,
) : FeatureDefinition {

    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime =
        SimpleFeatureRuntime.whichHandle(id, action)

    override fun getSpecificStates(stateProvider: StateProvider): List<State> = emptyList()
}