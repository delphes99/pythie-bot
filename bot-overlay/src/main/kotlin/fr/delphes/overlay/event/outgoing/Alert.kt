package fr.delphes.overlay.event.outgoing

import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
import fr.delphes.dynamicForm.FieldDescription

@RegisterOutgoingEvent("overlay-alert")
data class Alert(
    @FieldDescription("Type of the alert")
    val alertType: String,
    @FieldDescription("Parameters")
    val parameters: Map<String, String>,
) : OverlayOutgoingEvent {
    constructor(type: String, vararg parameters: Pair<String, String>) : this(type, mapOf(*parameters))
}