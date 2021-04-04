package fr.delphes.connector.twitch

import fr.delphes.connector.twitch.command.Command
import fr.delphes.feature.Feature
import fr.delphes.feature.FeatureDescription
import fr.delphes.twitch.TwitchChannel

abstract class TwitchFeature<DESC : FeatureDescription>(
    val channel: TwitchChannel
) : Feature<DESC> {
    open val commands: Iterable<Command> = emptyList()
}