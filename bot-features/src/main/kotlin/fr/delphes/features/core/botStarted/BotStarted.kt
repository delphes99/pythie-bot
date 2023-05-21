package fr.delphes.features.core.botStarted

import fr.delphes.bot.event.eventHandler.EventHandlerContext
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.BotStarted
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider

class BotStarted(
    override val id: FeatureId = FeatureId(),
    private val listener: EventHandlerContext<BotStarted>,
) : FeatureDefinition {
    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime {
        val eventHandlers = EventHandlers.of<BotStarted>(listener)

        return SimpleFeatureRuntime(eventHandlers, id)
    }

    override fun getSpecificStates(stateProvider: StateProvider) = emptyList<State>()
}