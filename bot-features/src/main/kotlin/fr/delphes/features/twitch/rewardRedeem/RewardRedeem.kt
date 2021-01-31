package fr.delphes.features.twitch.rewardRedeem

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.TwitchFeature
import fr.delphes.twitch.api.reward.WithRewardConfiguration

class RewardRedeem(
    channel: String,
    private val rewardConfiguration: WithRewardConfiguration,
    private val rewardRedeemResponse: (RewardRedemption) -> List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(RewardRedemptionHandler())
    }

    inner class RewardRedemptionHandler : EventHandler<RewardRedemption> {
        override suspend fun handle(event: RewardRedemption, channel: ChannelInfo): List<OutgoingEvent> {
            return if(event.reward.rewardConfiguration == rewardConfiguration.rewardConfiguration) {
                rewardRedeemResponse(event)
            } else {
                emptyList()
            }
        }
    }
}