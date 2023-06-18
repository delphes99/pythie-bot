package fr.delphes.overlay.event.outgoing

data class Alert(val type: String, val parameters: Map<String, String>) : OverlayOutgoingEvent {
    constructor(type: String, vararg parameters: Pair<String, String>) : this(type, mapOf(*parameters))
}