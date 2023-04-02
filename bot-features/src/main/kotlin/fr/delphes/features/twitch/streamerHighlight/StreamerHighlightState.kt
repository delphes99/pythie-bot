@file:UseSerializers(LocalDateTimeSerializer::class)

package fr.delphes.features.twitch.streamerHighlight

import fr.delphes.feature.State
import fr.delphes.twitch.api.user.UserName
import fr.delphes.utils.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class StreamerHighlightState(
    val streamerHighlighted: Map<String, LocalDateTime> = mapOf(),
) : State {
    fun isAlreadyHighlighted(user: UserName, isExpired: (LocalDateTime) -> Boolean): Boolean {
        return streamerHighlighted[user.normalizeName]?.let(isExpired) ?: false
    }

    fun highlight(user: UserName, highlightDate: LocalDateTime): StreamerHighlightState {
        val newState = this.streamerHighlighted + (user.normalizeName to highlightDate)
        return StreamerHighlightState(
            newState
        )
    }
}