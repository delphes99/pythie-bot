package fr.delphes.features.twitch.streamUpdated

import fr.delphes.bot.event.eventHandler.IncomingEventHandlerAction
import fr.delphes.connector.twitch.incomingEvent.StreamChanged
import fr.delphes.features.twitch.SimpleTwitchEventFeature
import fr.delphes.rework.feature.FeatureId
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomStreamUpdated(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    action: IncomingEventHandlerAction<StreamChanged>,
) : SimpleTwitchEventFeature<StreamChanged>(StreamChanged::class, id, action) {
    override fun getSpecificStates(stateProvider: StateProvider) = emptyList<State>()
}