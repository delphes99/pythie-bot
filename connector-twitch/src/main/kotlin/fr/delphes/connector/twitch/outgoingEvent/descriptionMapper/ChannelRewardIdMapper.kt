package fr.delphes.connector.twitch.outgoingEvent.descriptionMapper

import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.connector.twitch.reward.RewardTitle
import fr.delphes.generation.dynamicForm.FieldDescriptorMapper
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