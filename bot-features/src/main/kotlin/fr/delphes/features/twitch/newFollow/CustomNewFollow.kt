package fr.delphes.features.twitch.newFollow

import fr.delphes.bot.event.eventHandler.EventHandlerContext
import fr.delphes.connector.twitch.incomingEvent.NewFollow
import fr.delphes.features.twitch.SimpleTwitchEventFeature
import fr.delphes.rework.feature.FeatureId
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomNewFollow(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    action: EventHandlerContext<NewFollow>,
) : SimpleTwitchEventFeature<NewFollow>(NewFollow::class, id, action) {
    override fun getSpecificStates(stateProvider: StateProvider) = emptyList<State>()
}
