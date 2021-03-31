package fr.delphes.features.twitch

import fr.delphes.feature.FeatureDescription

interface TwitchFeatureDescription : FeatureDescription {
    val channel: String
}