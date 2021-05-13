package fr.delphes.bot.event.outgoing

import java.time.Duration

data class Pause(val delay: Duration) : CoreOutgoingEvent
