package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName

data class IncomingRaid(
    override val channel: TwitchChannel,
    val leader: UserName,
    val numberOfRaiders: Long
): TwitchIncomingEvent