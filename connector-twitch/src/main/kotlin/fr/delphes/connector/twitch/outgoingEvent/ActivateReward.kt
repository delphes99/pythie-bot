package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.WithRewardConfiguration

data class ActivateReward(
    val reward: WithRewardConfiguration,
    override val channel: TwitchChannel
) : TwitchApiOutgoingEvent {
    override suspend fun executeOnTwitch(
        twitchApi: ChannelTwitchApi,
        connector: TwitchConnector
    ) {
        twitchApi.activateReward(reward.rewardConfiguration)
    }
}