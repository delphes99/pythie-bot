package fr.delphes.connector.twitch.feature

import fr.delphes.feature.featureNew.FeatureConfiguration
import fr.delphes.feature.featureNew.FeatureState
import fr.delphes.twitch.TwitchChannel

interface TwitchFeatureConfiguration<STATE: FeatureState> : FeatureConfiguration<STATE> {
    val channel: TwitchChannel?
}