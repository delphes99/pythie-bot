package fr.delphes.connector.twitch

import fr.delphes.bot.Bot

sealed class TwitchStateEvent {
    data class Connect(val bot: Bot) : TwitchStateEvent()

    data class Configure(
        val configuration: TwitchConfiguration,
        val connector: TwitchConnector
    ) : TwitchStateEvent()
}