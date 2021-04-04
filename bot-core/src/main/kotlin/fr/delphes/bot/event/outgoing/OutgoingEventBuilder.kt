package fr.delphes.bot.event.outgoing

interface OutgoingEventBuilder {
    fun build() : OutgoingEvent
}
