package fr.delphes.connector.twitch.reward

import fr.delphes.connector.twitch.api.TwitchApi
import fr.delphes.twitch.TwitchChannel
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk

private val twitchApi = mockk<TwitchApi>()

class RewardServiceTest : ShouldSpec({
    val channel = TwitchChannel("channel")
    val rewardId = RewardId(channel, RewardTitle("title"))
    val rewardConfiguration = RewardConfiguration(
        10,
        "prompt"
    )
    val configuredReward = ConfiguredReward(rewardId, rewardConfiguration)

    beforeAny {
        clearAllMocks()
    }

    context("getReward") {
        should("unknown reward") {
            rewardId `have twitch reward` null

            val service = RewardService(
                twitchApi = twitchApi,
                configuredRewards = ConfiguredRewards()
            )

            service.getReward(rewardId) shouldBe UnknownReward(rewardId)
        }

        should("configured reward but not in twitch") {
            rewardId `have twitch reward` null

            val service = RewardService(
                twitchApi = twitchApi,
                configuredRewards = ConfiguredRewards(configuredReward)
            )

            service.getReward(rewardId) shouldBe NotDeployedReward(
                rewardId,
                rewardConfiguration
            )
        }

        should("unconfigured reward but in twitch") {
            rewardId `have twitch reward` InTwitchReward(
                rewardId,
                rewardConfiguration
            )

            val service = RewardService(
                twitchApi = twitchApi,
                configuredRewards = ConfiguredRewards()
            )

            service.getReward(rewardId) shouldBe UnmanagedReward(
                rewardId,
                rewardConfiguration
            )
        }

        should("configured reward synch twitch") {
            rewardId `have twitch reward` InTwitchReward(
                rewardId,
                rewardConfiguration
            )

            val service = RewardService(
                twitchApi = twitchApi,
                configuredRewards = ConfiguredRewards(configuredReward)
            )

            service.getReward(rewardId) shouldBe SynchronizedReward(
                rewardId,
                rewardConfiguration
            )
        }
    }

    context("getRewards") {
        should("no rewards") {
            `given twitch rewards`(emptyList())

            val service = RewardService(
                twitchApi = twitchApi,
                configuredRewards = ConfiguredRewards()
            )

            service.getRewards() shouldBe emptyList()
        }
        should("unconfigured rewards (in twitch but not in bot configurations)") {
            `given twitch rewards`(
                listOf(
                    InTwitchReward(
                        RewardId(channel, RewardTitle("unconfigured")),
                        rewardConfiguration
                    )
                )
            )

            val service = RewardService(
                twitchApi = twitchApi,
                configuredRewards = ConfiguredRewards()
            )

            service.getRewards().shouldContainAll(
                UnmanagedReward(
                    RewardId(channel, RewardTitle("unconfigured")),
                    rewardConfiguration
                )
            )
        }
        should("not deployed rewards (not in twitch but in bot configurations)") {
            `given twitch rewards`(emptyList())

            val service = RewardService(
                twitchApi = twitchApi,
                configuredRewards = ConfiguredRewards(configuredReward)
            )

            service.getRewards().shouldContainAll(
                NotDeployedReward(
                    rewardId,
                    rewardConfiguration
                )
            )
        }
        should("synchronized rewards (in twitch and in bot configurations)") {
            `given twitch rewards`(listOf(InTwitchReward(rewardId, rewardConfiguration)))

            val service = RewardService(
                twitchApi = twitchApi,
                configuredRewards = ConfiguredRewards(configuredReward)
            )

            service.getRewards().shouldContainAll(
                SynchronizedReward(
                    rewardId,
                    rewardConfiguration
                )
            )
        }
    }
})

private fun `given twitch rewards`(emptyList: List<InTwitchReward>) {
    coEvery { twitchApi.getRewards() } returns emptyList
}

private infix fun RewardId.`have twitch reward`(
    inTwitchReward: InTwitchReward?,
) {
    coEvery { twitchApi.getCustomRewards(this@`have twitch reward`) } returns inTwitchReward
}