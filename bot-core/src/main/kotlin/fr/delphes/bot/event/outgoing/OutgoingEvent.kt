package fr.delphes.bot.event.outgoing

interface OutgoingEvent

data class Alert(val type: String, val parameters: Map<String, String>): OutgoingEvent {
    constructor(type: String, vararg parameters: Pair<String, String>): this(type, mapOf(*parameters))
}