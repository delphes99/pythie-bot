package fr.delphes.overlay.event.outgoing

import java.time.Duration

data class Pause(val delay: Duration) : OverlayOutgoingEvent
