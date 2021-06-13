package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User

data class IncomingRaid(
    override val channel: TwitchChannel,
    val leader: User,
    val numberOfRaiders: Long
): TwitchIncomingEvent