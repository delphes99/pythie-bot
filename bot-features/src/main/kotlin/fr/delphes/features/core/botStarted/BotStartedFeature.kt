package fr.delphes.features.core.botStarted

import fr.delphes.bot.event.eventHandler.EventHandlerContext
import fr.delphes.bot.event.incoming.BotStarted
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider

class BotStartedFeature(
    override val id: FeatureId = FeatureId(),
    private val action: EventHandlerContext<BotStarted>,
) : FeatureDefinition {
    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime =
        SimpleFeatureRuntime.whichHandle(id, action)

    override fun getSpecificStates(stateProvider: StateProvider) = emptyList<State>()
}