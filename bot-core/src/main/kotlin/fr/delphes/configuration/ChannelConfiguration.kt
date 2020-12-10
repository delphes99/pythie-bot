package fr.delphes.configuration

import fr.delphes.feature.Feature
import fr.delphes.twitch.api.reward.RewardConfiguration
import java.util.Properties

data class ChannelConfiguration(
    val ownerChannel: String,
    val ownerAccountOauth: String,
    val rewards: List<RewardConfiguration>,
    val features: List<Feature>
) {
    constructor(
        ownerChannel: String,
        ownerAccountOauth: String,
        rewards: List<RewardConfiguration>,
        vararg features: Feature
    ) : this(ownerChannel, ownerAccountOauth, rewards, listOf(*features))

    companion object {
        fun build(
            path: String,
            buildConfiguration: (Properties) -> ChannelConfiguration
        ): ChannelConfiguration {
            return buildConfiguration(loadProperties(path))
        }
    }
}
