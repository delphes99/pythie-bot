package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel

data class NewPoll(
    override val channel: TwitchChannel,
    val poll: Poll
) : TwitchIncomingEvent