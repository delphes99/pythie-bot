package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.Channel
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.irc.IrcClient

sealed interface TwitchOutgoingEvent : OutgoingEvent {
    val channel: TwitchChannel

    suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    )
}