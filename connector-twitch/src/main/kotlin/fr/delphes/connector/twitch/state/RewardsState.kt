package fr.delphes.connector.twitch.state

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.reward.NotDeployedReward
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.connector.twitch.reward.RewardTitle
import fr.delphes.connector.twitch.reward.RunTimeReward
import fr.delphes.connector.twitch.reward.SynchronizedReward
import fr.delphes.connector.twitch.reward.UnknownReward
import fr.delphes.connector.twitch.reward.UnmanagedReward
import fr.delphes.state.enumeration.EnumStateId
import fr.delphes.state.enumeration.EnumStateItem
import fr.delphes.state.enumeration.EnumerationState
import fr.delphes.twitch.TwitchChannel

class RewardsState(
    private val connector: TwitchConnector,
) : EnumerationState<RewardId> {
    override val id = EnumStateId("rewards")

    override suspend fun getItems(): List<EnumStateItem> {
        return connector.rewardService.getRewards().map {
            EnumStateItem(
                it.toValue(),
                it.id.title.title,
                it.toDescription()
            )
        }
    }

    override fun deserialize(serializeValue: String): RewardId {
        serializeValue.split("|:|", limit = 2).let {
            return RewardId(
                TwitchChannel(it[0]),
                RewardTitle(it[1])
            )
        }
    }

    private fun RunTimeReward.toValue() = "${id.channel.name}|:|${id.title.title}"

    private fun RunTimeReward.toDescription() = when (this) {
        is NotDeployedReward -> rewardConfiguration.prompt
        is SynchronizedReward -> rewardConfiguration.prompt
        is UnmanagedReward -> rewardConfiguration.prompt
        is UnknownReward -> throw IllegalStateException("Reward must be known")
    } ?: ""
}
