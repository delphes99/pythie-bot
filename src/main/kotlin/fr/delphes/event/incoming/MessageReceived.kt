package fr.delphes.event.incoming

import fr.delphes.User

data class MessageReceived(
    val user: User,
    val text: String
): IncomingEvent