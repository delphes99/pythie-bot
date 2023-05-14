package fr.delphes.connector.twitch.state

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.state.StateId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.WithRewardConfiguration

class ChannelRewardsState(
    private val channel: TwitchChannel,
    connector: TwitchConnector,
) : TwitchChannelApiConnectorState(connector) {
    override val id: StateId<ChannelRewardsState> = id(channel)

    suspend fun getReward(rewardName: String): WithRewardConfiguration? {
        return whenRunning(channel) {
            channelTwitchApi
                .getRewards()
                .firstOrNull { it.rewardConfiguration.title == rewardName }
        }
    }

    companion object {
        fun id(qualifier: TwitchChannel) = StateId.from<ChannelRewardsState>(qualifier.normalizeName)
    }
}