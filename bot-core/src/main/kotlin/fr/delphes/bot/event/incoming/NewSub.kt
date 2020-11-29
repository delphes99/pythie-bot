package fr.delphes.bot.event.incoming

import fr.delphes.twitch.api.user.User

data class NewSub(
    val sub: User
) : IncomingEvent