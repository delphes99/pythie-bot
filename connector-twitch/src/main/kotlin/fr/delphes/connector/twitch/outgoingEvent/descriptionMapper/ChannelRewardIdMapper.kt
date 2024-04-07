package fr.delphes.connector.twitch.outgoingEvent.descriptionMapper

import fr.delphes.generation.dynamicForm.FieldDescriptorMapper
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.connector.twitch.reward.RewardTitle
import fr.delphes.twitch.TwitchChannel

object ChannelRewardIdMapper : FieldDescriptorMapper<RewardId> {
    override suspend fun map(value: String): RewardId {
        return value.split("::").let { (channel, title) ->
            RewardId(
                TwitchChannel(channel),
                RewardTitle(title)
            )
        }
    }
}