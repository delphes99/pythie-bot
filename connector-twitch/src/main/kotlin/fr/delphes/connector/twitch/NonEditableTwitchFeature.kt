package fr.delphes.connector.twitch

import fr.delphes.connector.twitch.command.Command
import fr.delphes.feature.NonEditableFeature
import fr.delphes.feature.NonEditableFeatureDescription
import fr.delphes.twitch.TwitchChannel

abstract class NonEditableTwitchFeature<DESC : NonEditableFeatureDescription>(
    val channel: TwitchChannel
) : NonEditableFeature<DESC> {
    open val commands: Iterable<Command> = emptyList()
}