package fr.delphes.bot.event.incoming

import fr.delphes.twitch.api.user.User

data class NewFollow(
    val follower: User
) : IncomingEvent