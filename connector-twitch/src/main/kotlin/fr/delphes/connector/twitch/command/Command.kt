package fr.delphes.connector.twitch.command

import kotlinx.serialization.Serializable

@Serializable
data class Command(
    val triggerMessage: String
)