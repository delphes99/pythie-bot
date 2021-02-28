@file:UseSerializers(LocalDateTimeSerializer::class)

package fr.delphes.features.twitch.streamerHighlight

import fr.delphes.feature.State
import fr.delphes.twitch.api.user.User
import fr.delphes.utils.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class StreamerHighlightState(
    val streamerHighlighted: MutableMap<String, LocalDateTime> = mutableMapOf(),
) : State {
    fun isAlreadyHighlighted(user: User, isExpired: (LocalDateTime) -> Boolean): Boolean {
        return streamerHighlighted[user.normalizeName]?.let(isExpired) ?: false
    }

    fun highlight(user: User, highlightDate: LocalDateTime) {
        streamerHighlighted[user.normalizeName] = highlightDate
    }
}