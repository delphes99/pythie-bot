package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.irc.IrcClient

sealed interface TwitchChatOutgoingEvent : TwitchOutgoingEvent {
    suspend fun executeOnTwitch(
        chat: IrcClient,
        connector: TwitchConnector,
    )
}