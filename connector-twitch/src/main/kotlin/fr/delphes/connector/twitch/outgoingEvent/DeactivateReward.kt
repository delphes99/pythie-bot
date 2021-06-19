package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.connector.twitch.Channel
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.WithRewardConfiguration
import fr.delphes.twitch.irc.IrcClient

data class DeactivateReward(
    val reward: WithRewardConfiguration,
    override val channel: TwitchChannel
) : TwitchOutgoingEvent {
    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    ) {
        twitchApi.deactivateReward(reward.rewardConfiguration)
    }
}