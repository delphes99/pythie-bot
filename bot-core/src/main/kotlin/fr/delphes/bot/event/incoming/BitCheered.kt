package fr.delphes.bot.event.incoming

import fr.delphes.twitch.api.user.User

data class BitCheered(
    val cheerer: User?,
    val bitsUsed: Long,
    val message: String?
) : IncomingEvent