package fr.delphes.overlay.event.outgoing

import fr.delphes.annotation.dynamicForm.FieldDescription

data class PlaySound(
    @FieldDescription("file to play")
    val mediaName: String,
) : OverlayOutgoingEvent
