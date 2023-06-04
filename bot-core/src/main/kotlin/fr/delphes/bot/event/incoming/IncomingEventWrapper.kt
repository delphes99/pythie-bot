@file:UseSerializers(LocalDateTimeSerializer::class)

package fr.delphes.bot.event.incoming

import fr.delphes.utils.serialization.LocalDateTimeSerializer
import fr.delphes.utils.time.SystemClock
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class IncomingEventWrapper<T : IncomingEvent>(
    val data: T,
    val date: LocalDateTime = SystemClock.now(),
    val replay: IncomingEventId? = null,
    val id: IncomingEventId = IncomingEventId(),
) {
    fun isReplay(): Boolean = replay != null
}