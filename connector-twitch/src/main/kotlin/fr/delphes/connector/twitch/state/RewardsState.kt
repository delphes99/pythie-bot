package fr.delphes.connector.twitch.state

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.reward.ChannelRewardId
import fr.delphes.state.StateId
import fr.delphes.twitch.api.reward.WithRewardConfiguration

class RewardsState(
    connector: TwitchConnector,
) : TwitchChannelApiConnectorState(connector) {
    override val id: StateId<RewardsState> = ID

    suspend fun getReward(id: ChannelRewardId): WithRewardConfiguration? {
        return whenRunning(id.channel) {
            channelTwitchApi
                .getRewards()
                .firstOrNull { it.rewardConfiguration.title == id.title }
        }
    }

    companion object {
        val ID = StateId.from<RewardsState>("rewards")
    }
}