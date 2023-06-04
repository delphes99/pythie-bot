package fr.delphes.bot.monitoring

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.IncomingEventWrapper
import kotlinx.serialization.Serializable

@Serializable
class StatisticIncomingEventData<T : IncomingEvent>(
    val incomingEvent: IncomingEventWrapper<T>,
) : StatisticData