package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.annotation.RegisterIncomingEvent
import fr.delphes.connector.twitch.command.Command
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import kotlinx.serialization.Serializable

@Serializable
@RegisterIncomingEvent
data class CommandAsked(
    override val channel: TwitchChannel,
    val command: Command,
    val by: UserName,
    val parameters: List<String> = emptyList(),
) : TwitchIncomingEvent