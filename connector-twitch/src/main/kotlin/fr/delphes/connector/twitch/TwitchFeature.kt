package fr.delphes.connector.twitch

import fr.delphes.connector.twitch.command.Command
import fr.delphes.twitch.TwitchChannel

//TODO implement FeatureDefinition
interface TwitchFeature {
    val channel: TwitchChannel

    val commands: Iterable<Command> get() = emptyList()
}