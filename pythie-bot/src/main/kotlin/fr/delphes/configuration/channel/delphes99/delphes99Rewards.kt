package fr.delphes.configuration.channel.delphes99

import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.WithRewardConfiguration

enum class DelphesReward(override val rewardConfiguration: RewardConfiguration): WithRewardConfiguration {
    VOTH(vothReward),
    DEV_TEST(testDev),
    DEV_TEST2(testDev2),
    SATISFACTORY_COLOR(satisfactoryBaseColor);

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
private val testDev2 = RewardConfiguration(
    "Test dev 2",
    1,
    "Test lors des live coding"
)
private val satisfactoryBaseColor = RewardConfiguration(
    "Couleur de la base",
    1500,
    "Choisissez la couleur de la base (pour le meilleur ou pour le pire...)",
    isGlobalCooldownEnabled = true,
    globalCooldownSeconds = 30 * 60
)