package fr.delphes.connector.twitch.outgoingEvent.descriptionMapper

import fr.delphes.annotation.outgoingEvent.createBuilder.FieldDescriptorMapper
import fr.delphes.connector.twitch.reward.ChannelRewardId
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel

object ChannelRewardIdMapper : FieldDescriptorMapper<ChannelRewardId> {
    override suspend fun map(value: String, stateProvider: StateProvider): ChannelRewardId {
        return value.split("::").let { (channel, title) ->
            ChannelRewardId(
                title,
                TwitchChannel(channel)
            )
        }
    }
}