package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName

data class NewFollow(
    override val channel: TwitchChannel,
    val follower: UserName
) : TwitchIncomingEvent