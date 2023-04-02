package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.ChannelTwitchApi

sealed interface TwitchApiOutgoingEvent: TwitchOutgoingEvent {
    suspend fun executeOnTwitch(
        twitchApi: ChannelTwitchApi,
        connector: TwitchConnector,
    )
}