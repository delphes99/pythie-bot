package fr.delphes.connector.twitch.state

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.connector.twitch.reward.RunTimeReward
import fr.delphes.state.StateId

class RewardsState(
    connector: TwitchConnector,
) : TwitchChannelApiConnectorState(connector) {
    override val id: StateId<RewardsState> = ID

    suspend fun getReward(id: RewardId): RunTimeReward {
        return connector.rewardService.getReward(id)
    }

    companion object {
        val ID = StateId.from<RewardsState>("rewards")
    }
}