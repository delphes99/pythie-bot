package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
import fr.delphes.annotation.outgoingEvent.createBuilder.FieldDescription
import fr.delphes.annotation.outgoingEvent.createBuilder.FieldMapper
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.outgoingEvent.descriptionMapper.ChannelRewardIdMapper
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.connector.twitch.state.RewardsState
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel

@RegisterOutgoingEvent("twitch-deactivate-reward")
data class DeactivateReward(
    @FieldDescription("reward to deactivate")
    @FieldMapper(ChannelRewardIdMapper::class)
    val reward: RewardId,
) : TwitchApiOutgoingEvent {
    override val channel: TwitchChannel = reward.channel

    override suspend fun executeOnTwitch(
        twitchApi: ChannelTwitchApi,
        connector: TwitchConnector,
    ) {
        val twitchReward = connector.bot.stateManager.getState(RewardsState.ID).getReward(reward)
            ?: error("Reward ${reward.title} not found")

        twitchApi.deactivateReward(twitchReward)
    }
}