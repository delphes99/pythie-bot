package fr.delphes.connector.twitch.state

import fr.delphes.twitch.TwitchChannel

interface BotAccountProvider {
    val botAccount: TwitchChannel?
}