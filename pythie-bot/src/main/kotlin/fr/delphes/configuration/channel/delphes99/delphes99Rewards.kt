package fr.delphes.configuration.channel.delphes99

import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.WithRewardConfiguration

enum class DelphesReward(override val rewardConfiguration: RewardConfiguration): WithRewardConfiguration {
    VOTH(vothReward),
    TEST_DEV(testDev);

    companion object {
        fun toRewardList() = values().map(DelphesReward::rewardConfiguration).toList()
    }
}

private val vothReward = RewardConfiguration(
    "VIP of the hill",
    300,
    "Prenez la place du VIP of hill : le dernier à réclamer cette récompense prends la place du VIP"
)
private val testDev = RewardConfiguration(
    "Test dev",
    1,
    "Test lors des live coding"
)