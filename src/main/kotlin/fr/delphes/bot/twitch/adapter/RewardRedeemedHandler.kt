package fr.delphes.bot.twitch.adapter

import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.bot.twitch.TwitchIncomingEventHandler

class RewardRedeemedHandler: TwitchIncomingEventHandler<RewardRedeemedEvent> {
    override fun transform(twitchEvent: RewardRedeemedEvent): List<IncomingEvent> {
        return listOf(RewardRedemption(twitchEvent))
    }
}