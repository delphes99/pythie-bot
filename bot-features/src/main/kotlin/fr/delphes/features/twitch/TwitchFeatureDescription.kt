package fr.delphes.features.twitch

import fr.delphes.feature.NonEditableFeatureDescription

interface TwitchFeatureDescription : NonEditableFeatureDescription {
    val channel: String
}