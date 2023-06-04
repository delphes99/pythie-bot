package fr.delphes.features.twitch.newSub

import fr.delphes.bot.event.eventHandler.IncomingEventHandlerAction
import fr.delphes.connector.twitch.incomingEvent.NewSub
import fr.delphes.features.twitch.SimpleTwitchEventFeature
import fr.delphes.rework.feature.FeatureId
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomNewSub(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    action: IncomingEventHandlerAction<NewSub>,
) : SimpleTwitchEventFeature<NewSub>(NewSub::class, id, action) {
    override fun getSpecificStates(stateProvider: StateProvider) = emptyList<State>()
}