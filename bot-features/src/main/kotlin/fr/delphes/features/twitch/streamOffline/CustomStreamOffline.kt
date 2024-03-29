package fr.delphes.features.twitch.streamOffline

import fr.delphes.bot.event.eventHandler.IncomingEventHandlerAction
import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.features.twitch.SimpleTwitchEventFeature
import fr.delphes.rework.feature.FeatureId
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomStreamOffline(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    action: IncomingEventHandlerAction<StreamOffline>,
) : SimpleTwitchEventFeature<StreamOffline>(StreamOffline::class, id, action) {
    override fun getSpecificStates(stateProvider: StateProvider) = emptyList<State>()
}