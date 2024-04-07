package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.FieldDescription
import fr.delphes.annotation.dynamicForm.FieldMapper
import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.outgoingEvent.descriptionMapper.ChannelRewardIdMapper
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel

@RegisterOutgoingEvent("twitch-activate-reward")
@DynamicForm("twitch-activate-reward-form", ["outgoing-event"])
data class ActivateReward(
    @FieldDescription("reward to activate")
    @FieldMapper(ChannelRewardIdMapper::class)
    val rewardId: RewardId,
) : TwitchApiOutgoingEvent {
    override val channel: TwitchChannel = rewardId.channel

    override suspend fun executeOnTwitch(
        twitchApi: ChannelTwitchApi,
        connector: TwitchConnector,
    ) {
        connector.rewardService.activateReward(rewardId)
    }
}