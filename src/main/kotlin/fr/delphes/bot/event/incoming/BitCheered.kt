package fr.delphes.bot.event.incoming

import fr.delphes.User

data class BitCheered(
    val cheerer: User,
    val bitsUsed: Long
) : IncomingEvent