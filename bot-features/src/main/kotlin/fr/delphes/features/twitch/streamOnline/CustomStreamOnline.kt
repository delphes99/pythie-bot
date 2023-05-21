package fr.delphes.features.twitch.streamOnline

import fr.delphes.bot.event.eventHandler.EventHandlerContext
import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.features.twitch.SimpleTwitchEventFeature
import fr.delphes.rework.feature.FeatureId
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomStreamOnline(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    action: EventHandlerContext<StreamOnline>,
) : SimpleTwitchEventFeature<StreamOnline>(StreamOnline::class, id, action) {
    override fun getSpecificStates(stateProvider: StateProvider) = emptyList<State>()
}