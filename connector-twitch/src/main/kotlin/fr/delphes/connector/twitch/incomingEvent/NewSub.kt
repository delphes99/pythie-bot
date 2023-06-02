package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import kotlinx.serialization.Serializable

@Serializable
data class NewSub(
    override val channel: TwitchChannel,
    val sub: UserName,
) : TwitchIncomingEvent