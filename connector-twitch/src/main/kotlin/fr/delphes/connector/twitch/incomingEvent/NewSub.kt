package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.annotation.RegisterIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import kotlinx.serialization.Serializable

@Serializable
@RegisterIncomingEvent
data class NewSub(
    override val channel: TwitchChannel,
    val sub: UserName,
) : TwitchIncomingEvent