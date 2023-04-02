package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName

data class BitCheered(
    override val channel: TwitchChannel,
    val cheerer: UserName?,
    val bitsUsed: Long,
    val message: String?
) : TwitchIncomingEvent