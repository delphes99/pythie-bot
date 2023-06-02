package fr.delphes.bot.monitoring

import fr.delphes.bot.event.incoming.IncomingEvent
import kotlinx.serialization.Serializable

@Serializable
class StatisticIncomingEventData<T : IncomingEvent>(
    val event: T,
) : StatisticData