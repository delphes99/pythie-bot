package fr.delphes.connector.twitch.command

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Command(
    val triggerMessage: String,
)