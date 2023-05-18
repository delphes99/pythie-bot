package fr.delphes.features.twitch.streamerHighlight

import fr.delphes.state.State
import fr.delphes.state.StateId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import java.time.LocalDateTime

class StreamerHighlightState(
    private val channel: TwitchChannel,
    private val streamerHighlighted: MutableMap<UserName, LocalDateTime> = mutableMapOf(),
) : State {
    override val id = idFor(channel)

    fun isAlreadyHighlighted(user: UserName, isExpired: (LocalDateTime) -> Boolean): Boolean {
        return streamerHighlighted[user]?.let(isExpired) ?: false
    }

    fun highlight(user: UserName, highlightDate: LocalDateTime) {
        streamerHighlighted[user] = highlightDate
    }

    companion object {
        fun idFor(channel: TwitchChannel) = StateId.from<StreamerHighlightState>(channel.normalizeName)
    }
}