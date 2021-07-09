package fr.delphes.connector.twitch.state.action

import fr.delphes.connector.twitch.state.TwitchChannelAction
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User

data class MessageReceivedAction(
    override val channel: TwitchChannel,
    val user: User,
    val text: String
) : TwitchChannelAction