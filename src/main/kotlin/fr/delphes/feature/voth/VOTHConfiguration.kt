package fr.delphes.feature.voth

import fr.delphes.bot.event.outgoing.OutgoingEvent

data class VOTHConfiguration(
    val featureId: String,
    val newVipAnnouncer: (NewVOTHAnnounced) -> List<OutgoingEvent>,
    val statsCommand: String,
    val statsResponseEvents: (Stats) -> List<OutgoingEvent>
)