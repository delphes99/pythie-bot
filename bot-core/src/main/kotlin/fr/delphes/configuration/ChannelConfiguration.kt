package fr.delphes.configuration

import fr.delphes.twitch.api.reward.RewardConfiguration
import java.util.Properties

data class ChannelConfiguration(
    val ownerChannel: String,
    val ownerAccountOauth: String,
    val rewards: List<RewardConfiguration>
) {
    companion object {
        fun build(
            path: String,
            buildConfiguration: (Properties) -> ChannelConfiguration
        ): ChannelConfiguration {
            return buildConfiguration(loadProperties(path))
        }
    }
}
