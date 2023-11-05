package fr.delphes.connector.twitch.state

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.state.StateId
import fr.delphes.twitch.api.reward.TwitchRewardConfiguration

class RewardsState(
    connector: TwitchConnector,
) : TwitchChannelApiConnectorState(connector) {
    override val id: StateId<RewardsState> = ID

    suspend fun getReward(id: RewardId): TwitchRewardConfiguration? {
        return whenRunning(id.channel) {
            channelTwitchApi
                .getRewards()
                .firstOrNull { it.title == id.title.title }
        }
    }

    companion object {
        val ID = StateId.from<RewardsState>("rewards")
    }
}