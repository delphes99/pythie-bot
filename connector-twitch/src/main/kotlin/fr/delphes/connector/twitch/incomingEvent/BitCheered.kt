package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.annotation.incomingEvent.RegisterIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import kotlinx.serialization.Serializable

@Serializable
@RegisterIncomingEvent
data class BitCheered(
    override val channel: TwitchChannel,
    val cheerer: UserName?,
    val bitsUsed: Long,
    val message: String?,
) : TwitchIncomingEvent