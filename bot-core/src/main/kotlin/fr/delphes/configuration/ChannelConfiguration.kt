package fr.delphes.configuration

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.TwitchRewardConfiguration
import java.util.Properties

//TODO add channel to reward configuration to remove this
data class ChannelConfiguration(
    val channel: TwitchChannel,
    val rewards: List<TwitchRewardConfiguration>,
) {
    companion object {
        fun build(
            path: String,
            buildConfiguration: (Properties) -> ChannelConfiguration,
        ): ChannelConfiguration {
            return buildConfiguration(loadProperties(path))
        }
    }
}
