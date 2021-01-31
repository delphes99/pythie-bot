package fr.delphes.bot.event.incoming

import fr.delphes.twitch.TwitchChannel

data class StreamOffline(
    override val channel: TwitchChannel
) : TwitchIncomingEvent