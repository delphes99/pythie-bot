@file:UseSerializers(LocalDateTimeSerializer::class)

package fr.delphes.bot.connector.statistics

import fr.delphes.bot.connector.ConnectorType
import fr.delphes.utils.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
class StatEvent<DATA : StatisticEventData>(
    val connector: ConnectorType,
    val date: LocalDateTime,
    val event: DATA,
)

interface StatisticEventData