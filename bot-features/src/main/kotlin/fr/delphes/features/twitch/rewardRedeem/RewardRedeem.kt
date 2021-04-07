package fr.delphes.features.twitch.rewardRedeem

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.feature.NonEditableFeature
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.WithRewardConfiguration

class RewardRedeem(
    override val channel: TwitchChannel,
    private val rewardConfiguration: WithRewardConfiguration,
    private val rewardRedeemResponse: (RewardRedemption) -> List<OutgoingEvent>
) : NonEditableFeature<RewardRedeemDescription>, TwitchFeature {
    override fun description() = RewardRedeemDescription(channel.name)

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(RewardRedemptionHandler())
    }

    inner class RewardRedemptionHandler : TwitchEventHandler<RewardRedemption>(channel) {
        override suspend fun handleIfGoodChannel(event: RewardRedemption, bot: Bot): List<OutgoingEvent> {
            return if(rewardConfiguration.rewardConfiguration.match(event.reward)) {
                rewardRedeemResponse(event)
            } else {
                emptyList()
            }
        }
    }
}