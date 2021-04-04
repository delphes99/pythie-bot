package fr.delphes.features.twitch

import fr.delphes.feature.EditableFeatureDescription

interface EditableTwitchFeatureDescription : EditableFeatureDescription {
    val channel: String
}