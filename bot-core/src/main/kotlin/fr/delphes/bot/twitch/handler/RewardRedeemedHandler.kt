package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.bot.event.incoming.TwitchIncomingEvent
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardRedemption as RewardRedemptionTwitch

class RewardRedeemedHandler(bot: ClientBot) : TwitchIncomingEventHandler<RewardRedemptionTwitch> {
    override suspend fun handle(
        twitchEvent: fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardRedemption
    ): List<TwitchIncomingEvent> {
        return listOf(
            RewardRedemption(
                twitchEvent.channel,
                twitchEvent
            )
        )
    }
}