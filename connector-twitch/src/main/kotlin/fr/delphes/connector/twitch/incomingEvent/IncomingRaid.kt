package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.annotation.RegisterIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import kotlinx.serialization.Serializable

@Serializable
@RegisterIncomingEvent
data class IncomingRaid(
    override val channel: TwitchChannel,
    val leader: UserName,
    val numberOfRaiders: Long,
) : TwitchIncomingEvent