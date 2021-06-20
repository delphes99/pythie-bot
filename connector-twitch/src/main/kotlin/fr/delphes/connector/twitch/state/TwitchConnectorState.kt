package fr.delphes.connector.twitch.state

import fr.delphes.bot.state.UserMessage
import kotlinx.serialization.Serializable

@Serializable
data class TwitchConnectorState(
    val userMessages: List<UserMessage> = emptyList()
)
