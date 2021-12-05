package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.WithRewardConfiguration

data class DeactivateReward(
    val reward: WithRewardConfiguration,
    override val channel: TwitchChannel
) : TwitchApiOutgoingEvent {
    override suspend fun executeOnTwitch(
        twitchApi: ChannelTwitchApi
    ) {
        twitchApi.deactivateReward(reward.rewardConfiguration)
    }
}