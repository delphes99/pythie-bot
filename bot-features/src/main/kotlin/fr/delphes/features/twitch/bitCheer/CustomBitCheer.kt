package fr.delphes.features.twitch.bitCheer

import fr.delphes.bot.event.eventHandler.EventHandlerContext
import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.features.twitch.SimpleTwitchEventFeature
import fr.delphes.rework.feature.FeatureId
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomBitCheer(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    action: EventHandlerContext<BitCheered>,
) : SimpleTwitchEventFeature<BitCheered>(BitCheered::class, id, action) {
    override fun getSpecificStates(stateProvider: StateProvider) = emptyList<State>()
}