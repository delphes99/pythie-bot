package fr.delphes.connector.twitch

import fr.delphes.connector.twitch.command.Command
import fr.delphes.feature.EditableFeature
import fr.delphes.feature.FeatureDescription
import fr.delphes.twitch.TwitchChannel

abstract class EditableTwitchFeature<DESC : FeatureDescription>(
    val channel: TwitchChannel
) : EditableFeature<DESC> {
    open val commands: Iterable<Command> = emptyList()
}