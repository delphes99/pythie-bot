@file:UseSerializers(LocalDateTimeSerializer::class)

package fr.delphes.bot.monitoring

import fr.delphes.utils.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
class StatisticEvent<DATA : StatisticData>(
    val date: LocalDateTime,
    val event: DATA,
)