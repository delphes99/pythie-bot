package fr.delphes.connector.twitch.outgoingEvent.descriptionMapper

import fr.delphes.annotation.dynamicForm.FieldDescriptorMapper
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.connector.twitch.reward.RewardTitle
import fr.delphes.twitch.TwitchChannel

object ChannelRewardIdMapper : FieldDescriptorMapper<RewardId> {
    override fun mapFromDto(value: String): RewardId {
        return value.split("::").let { (channel, title) ->
            RewardId(
                TwitchChannel(channel),
                RewardTitle(title)
            )
        }
    }

    override fun mapToDto(value: RewardId): String {
        return "${value.channel.name}::${value.title.title}"
    }
}