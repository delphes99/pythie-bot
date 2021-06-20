package fr.delphes.connector.twitch.state.action

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.utils.store.Action

data class MessageReceivedAction(
    val channel: TwitchChannel,
    val user: User,
    val text: String
) : Action