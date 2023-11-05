package fr.delphes.connector.twitch.reward

import fr.delphes.twitch.TwitchChannel

class ConfiguredRewards(
    val rewards: List<ConfiguredReward>,
) {
    companion object {
        fun builder(channel: TwitchChannel) = Builder(channel)
    }

    class Builder(
        private val channel: TwitchChannel,
        private val rewards: MutableList<ConfiguredReward> = mutableListOf(),
    ) {
        fun addReward(title: RewardTitle, configuration: RewardConfiguration): Builder {
            rewards.add(
                ConfiguredReward(
                    RewardId(channel, title),
                    configuration
                )
            )
            return this
        }

        fun build() = ConfiguredRewards(rewards)
    }
}