package fr.delphes.connector.twitch

import fr.delphes.connector.twitch.command.Command
import fr.delphes.twitch.TwitchChannel

interface TwitchFeature {
    val channel: TwitchChannel

    val commands: Iterable<Command> get() = emptyList()
}