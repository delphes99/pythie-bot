package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.twitch.irc.IrcClient

sealed interface TwitchOwnerChatOutgoingEvent : TwitchOutgoingEvent {
    suspend fun executeOnTwitch(
        chat: IrcClient,
    )
}