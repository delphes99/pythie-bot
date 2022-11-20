package fr.delphes

import fr.delphes.bot.event.incoming.IncomingEvent

data class FakeIncomingEvent(
    val value: String = "value"
): IncomingEvent
