package fr.delphes.bot.event.builder

import fr.delphes.bot.event.outgoing.OutgoingEvent

interface OutgoingEventBuilder {
    fun build(): OutgoingEvent
}
