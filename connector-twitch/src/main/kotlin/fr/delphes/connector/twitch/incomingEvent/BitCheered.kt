package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User

data class BitCheered(
    override val channel: TwitchChannel,
    val cheerer: User?,
    val bitsUsed: Long,
    val message: String?
) : TwitchIncomingEvent