@file:UseSerializers(LocalDateTimeSerializer::class)

package fr.delphes.twitch.api.clips

import fr.delphes.twitch.api.streams.ThumbnailUrl
import fr.delphes.twitch.api.user.UserName
import fr.delphes.utils.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class Clip(
    val url: String,
    val creator: UserName,
    val gameId: String,
    val title: String,
    val createdAt: LocalDateTime,
    val thumbnailUrl: ThumbnailUrl,
)