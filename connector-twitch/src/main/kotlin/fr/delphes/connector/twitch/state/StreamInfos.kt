@file:UseSerializers(LocalDateTimeSerializer::class)

package fr.delphes.connector.twitch.state

import fr.delphes.utils.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class StreamInfos(
    val id: String,
    val title: String,
    val start: LocalDateTime,
    val game: GameInfo,
)