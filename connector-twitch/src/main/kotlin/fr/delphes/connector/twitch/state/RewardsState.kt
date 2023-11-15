package fr.delphes.connector.twitch.state

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.reward.NotDeployedReward
import fr.delphes.connector.twitch.reward.RunTimeReward
import fr.delphes.connector.twitch.reward.SynchronizedReward
import fr.delphes.connector.twitch.reward.UnknownReward
import fr.delphes.connector.twitch.reward.UnmanagedReward
import fr.delphes.state.StateId
import fr.delphes.state.enumeration.EnumStateItem
import fr.delphes.state.enumeration.EnumerationState

class RewardsState(
    connector: TwitchConnector,
) : TwitchChannelApiConnectorState(connector), EnumerationState {
    override val id: StateId<RewardsState> = ID

    override suspend fun getItems(): List<EnumStateItem> {
        return connector.rewardService.getRewards().map {
            EnumStateItem(
                it.toValue(),
                it.id.title.title,
                it.toDescription()
            )
        }
    }

    private fun RunTimeReward.toValue() = "${id.channel.name} || ${id.title.title}"

    private fun RunTimeReward.toDescription() = when (this) {
        is NotDeployedReward -> rewardConfiguration.prompt
        is SynchronizedReward -> rewardConfiguration.prompt
        is UnmanagedReward -> rewardConfiguration.prompt
        is UnknownReward -> throw IllegalStateException("Reward must be known")
    } ?: ""

    companion object {
        val ID = StateId.from<RewardsState>("rewards")
    }
}
