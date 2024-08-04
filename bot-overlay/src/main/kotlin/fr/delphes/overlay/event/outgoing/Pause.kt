package fr.delphes.overlay.event.outgoing

import fr.delphes.annotation.dynamicForm.FieldDescription
import java.time.Duration

data class Pause(
    @FieldDescription("Duration before the next event")
    val delay: Duration,
) : OverlayOutgoingEvent
