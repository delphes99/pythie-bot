package fr.delphes.connector.twitch.feature

import fr.delphes.feature.featureNew.FeatureConfiguration
import fr.delphes.twitch.TwitchChannel

interface TwitchFeatureConfiguration : FeatureConfiguration {
    val channel: TwitchChannel
}