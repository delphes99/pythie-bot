package fr.delphes.features.discord

import fr.delphes.bot.event.eventHandler.IncomingEventHandlerAction
import fr.delphes.connector.discord.incomingEvent.NewGuildMember
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider

class NewGuildMemberFeature(
    override val id: FeatureId = FeatureId(),
    val action: IncomingEventHandlerAction<NewGuildMember>,
) : FeatureDefinition {
    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime =
        SimpleFeatureRuntime.whichHandle(id, action)

    override fun getSpecificStates(stateProvider: StateProvider) = emptyList<State>()
}