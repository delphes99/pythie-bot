package fr.delphes.connector.twitch.outgoingEvent.descriptionMapper

import fr.delphes.annotation.outgoingEvent.createBuilder.FieldDescriptorMapper
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.connector.twitch.reward.RewardTitle
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel

object ChannelRewardIdMapper : FieldDescriptorMapper<RewardId> {
    override suspend fun map(value: String, stateProvider: StateProvider): RewardId {
        return value.split("::").let { (channel, title) ->
            RewardId(
                TwitchChannel(channel),
                RewardTitle(title)
            )
        }
    }
}