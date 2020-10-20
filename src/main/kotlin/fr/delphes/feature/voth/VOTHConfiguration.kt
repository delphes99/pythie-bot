package fr.delphes.feature.voth

import fr.delphes.bot.event.outgoing.OutgoingEvent

data class VOTHConfiguration(
    val featureId: String,
    val newVipAnnouncer: (NewVOTHAnnounced) -> List<OutgoingEvent>,
    val statsCommand: String,
    val statsResponse: (Stats) -> List<OutgoingEvent>,
    val top3Command: String,
    val top3Response: (Stats?, Stats?, Stats?) -> List<OutgoingEvent>
)