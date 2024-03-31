package fr.delphes.overlay.event.outgoing

import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
import fr.delphes.dynamicForm.FieldDescription

@RegisterOutgoingEvent("overlay-play-sound")
data class PlaySound(
    @FieldDescription("file to play")
    val mediaName: String,
) : OverlayOutgoingEvent
