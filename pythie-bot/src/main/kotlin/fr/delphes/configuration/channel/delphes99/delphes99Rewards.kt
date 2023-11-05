package fr.delphes.configuration.channel.delphes99

import fr.delphes.connector.twitch.reward.ConfiguredReward
import fr.delphes.connector.twitch.reward.ConfiguredRewards
import fr.delphes.connector.twitch.reward.RewardConfiguration
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.connector.twitch.reward.RewardTitle
import java.time.Duration

object Delphes99Rewards {
    val VOTH = RewardId(channel, RewardTitle("VIP of the hill"))
    val ENTER_THE_MATRIX = RewardId(channel, RewardTitle("Enter the matrix"))
    val DEV_TEST = RewardId(channel, RewardTitle("Test dev"))
    val DEV_TEST2 = RewardId(channel, RewardTitle("Test dev 2"))
    val DEV_TEST3 = RewardId(channel, RewardTitle("Test dev 3"))
    val DEV_HORS_BOT = RewardId(channel, RewardTitle("Dev_hors_bot"))
    val SATISFACTORY_COLOR = RewardId(channel, RewardTitle("Couleur de la base"))
    val RIP = RewardId(channel, RewardTitle("R.I.P."))
    val WIZZ = RewardId(channel, RewardTitle("Wizz"))

    private val vothReward = RewardConfiguration(
        300,
        "Prenez la place du VIP of hill : le dernier à réclamer cette récompense prends la place du VIP"
    )
    private val enterTheMatrix = RewardConfiguration(
        150,
        "Pillule bleue ou pillule rouge"
    )
    private val testDev = RewardConfiguration(
        1,
        "Test lors des live coding",
        isGlobalCooldownEnabled = false
    )
    private val testDev2 = RewardConfiguration(
        1,
        "Test lors des live coding",
        isGlobalCooldownEnabled = false
    )
    private val testDev3 = RewardConfiguration(
        1,
        "Test lors des live coding",
        isGlobalCooldownEnabled = false
    )
    private val satisfactoryBaseColor = RewardConfiguration(
        1500,
        "Choisissez la couleur de la base (pour le meilleur ou pour le pire...)",
        isGlobalCooldownEnabled = true,
        globalCooldownSeconds = Duration.ofMinutes(30).toSeconds()
    )
    private val rip = RewardConfiguration(
        350,
        "Rest in peace",
        isGlobalCooldownEnabled = true,
        globalCooldownSeconds = Duration.ofSeconds(30).toSeconds()
    )
    private val wizz = RewardConfiguration(
        150,
        "Ok boomer",
        isGlobalCooldownEnabled = true,
        globalCooldownSeconds = Duration.ofSeconds(30).toSeconds()
    )

    val configuredRewards = mapOf(
        VOTH to vothReward,
        ENTER_THE_MATRIX to enterTheMatrix,
        DEV_TEST to testDev,
        DEV_TEST2 to testDev2,
        DEV_TEST3 to testDev3,
        SATISFACTORY_COLOR to satisfactoryBaseColor,
        RIP to rip,
        WIZZ to wizz,
    )
        .map { (id, configuration) -> ConfiguredReward(id, configuration) }
        .let { ConfiguredRewards(it) }
}