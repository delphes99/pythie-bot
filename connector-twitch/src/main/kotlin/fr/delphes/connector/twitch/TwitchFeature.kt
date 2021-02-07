package fr.delphes.connector.twitch

import fr.delphes.connector.twitch.command.Command
import fr.delphes.feature.Feature
import fr.delphes.twitch.TwitchChannel

abstract class TwitchFeature(
    val channel: TwitchChannel
) : Feature {
    open val commands: Iterable<Command> = emptyList()
}