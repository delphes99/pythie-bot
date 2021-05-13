package fr.delphes.connector.obs.incomingEvent

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.connector.obs.business.SourceFilter

data class SourceFilterDeactivated(
    val filter: SourceFilter,
) : IncomingEvent