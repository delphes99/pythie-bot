package fr.delphes.feature.voth

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.twitch.api.reward.WithRewardConfiguration

data class VOTHConfiguration(
    val reward: WithRewardConfiguration,
    val newVipAnnouncer: (NewVOTHAnnounced) -> List<OutgoingEvent>,
    val statsCommand: String,
    val statsResponse: (Stats) -> List<OutgoingEvent>,
    val top3Command: String,
    val top3Response: (Stats?, Stats?, Stats?) -> List<OutgoingEvent>
)