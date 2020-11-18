package fr.delphes.bot.event.incoming

import fr.delphes.twitch.model.User

data class BitCheered(
    val cheerer: User,
    val bitsUsed: Long
) : IncomingEvent