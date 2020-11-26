package fr.delphes.feature.rewardRedeem

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.AbstractFeature

class RewardRedeem(
    private val rewardName: String,
    private val rewardRedeemResponse: (RewardRedemption) -> List<OutgoingEvent>
) : AbstractFeature() {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(RewardRedemptionHandler())
    }

    inner class RewardRedemptionHandler : EventHandler<RewardRedemption> {
        override suspend fun handle(event: RewardRedemption, channel: ChannelInfo): List<OutgoingEvent> {
            return if(event.reward.isEquals(rewardName)) {
                rewardRedeemResponse(event)
            } else {
                emptyList()
            }
        }
    }
}