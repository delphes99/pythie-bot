package fr.delphes.overlay.event.outgoing

import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
import fr.delphes.dynamicForm.FieldDescription
import java.time.Duration

@RegisterOutgoingEvent("overlay-pause")
data class Pause(
    @FieldDescription("Duration before the next event")
    val delay: Duration,
) : OverlayOutgoingEvent
