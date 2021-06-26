package fr.delphes.connector.twitch.state

import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.store.Action

interface TwitchChannelAction: Action {
    val channel: TwitchChannel
}