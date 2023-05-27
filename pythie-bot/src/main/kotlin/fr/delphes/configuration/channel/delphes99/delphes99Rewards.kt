package fr.delphes.configuration.channel.delphes99

import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.WithRewardConfiguration
import java.time.Duration

enum class DelphesReward(override val rewardConfiguration: RewardConfiguration) : WithRewardConfiguration {
    VOTH(vothReward),
    ENTER_THE_MATRIX(enterTheMatrix),
    DEV_TEST(testDev),
    DEV_TEST2(testDev2),
    DEV_TEST3(testDev3),
    SATISFACTORY_COLOR(satisfactoryBaseColor),
    RIP(rip),
    WIZZ(wizz);

    companion object {
        fun toRewardList() = values().map(DelphesReward::rewardConfiguration).toList()
    }
}

private val vothReward = RewardConfiguration(
    "VIP of the hill",
    300,
    "Prenez la place du VIP of hill : le dernier à réclamer cette récompense prends la place du VIP"
)
private val enterTheMatrix = RewardConfiguration(
    "Enter the matrix",
    150,
    "Pillule bleue ou pillule rouge"
)
private val testDev = RewardConfiguration(
    "Test dev",
    1,
    "Test lors des live coding",
    isGlobalCooldownEnabled = false
)
private val testDev2 = RewardConfiguration(
    "Test dev 2",
    1,
    "Test lors des live coding",
    isGlobalCooldownEnabled = false
)
private val testDev3 = RewardConfiguration(
    "Test dev 3",
    1,
    "Test lors des live coding",
    isGlobalCooldownEnabled = false
)
private val satisfactoryBaseColor = RewardConfiguration(
    "Couleur de la base",
    1500,
    "Choisissez la couleur de la base (pour le meilleur ou pour le pire...)",
    isGlobalCooldownEnabled = true,
    globalCooldownSeconds = Duration.ofMinutes(30).toSeconds()
)
private val rip = RewardConfiguration(
    "R.I.P.",
    350,
    "Rest in peace",
    isGlobalCooldownEnabled = true,
    globalCooldownSeconds = Duration.ofSeconds(30).toSeconds()
)
private val wizz = RewardConfiguration(
    "Wizz",
    150,
    "Ok boomer",
    isGlobalCooldownEnabled = true,
    globalCooldownSeconds = Duration.ofSeconds(30).toSeconds()
)