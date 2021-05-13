package fr.delphes.bot.event.outgoing

data class Alert(val type: String, val parameters: Map<String, String>): CoreOutgoingEvent {
    constructor(type: String, vararg parameters: Pair<String, String>): this(type, mapOf(*parameters))
}