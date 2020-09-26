package fr.delphes.event.incoming

import fr.delphes.User

data class NewFollow(
    val follower: User
) : IncomingEvent