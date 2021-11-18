package fr.delphes.features.twitch.rewardRedeem

import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.RewardId
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class RewardRedeemTest {
    private val channel = TwitchChannel("channel")
    private val reward = RewardConfiguration("reward", 100)
    private val otherReward = RewardConfiguration("other_reward", 100)

    @Test
    internal suspend fun `respond if matching reward`() {
        val outgoingEvents = RewardRedeem(channel, reward) { listOf(mockk()) }.handleIncomingEvent(
            RewardRedemption(
                channel,
                RewardId("featureID", "reward"),
                "user",
                50
            ),
            mockk()
        )

        outgoingEvents.shouldContain(mockk())
    }

    @Test
    internal suspend fun `don't respond not matching reward`() {
        val outgoingEvents = RewardRedeem(channel, otherReward) { listOf(mockk()) }.handleIncomingEvent(
            RewardRedemption(
                channel,
                RewardId("featureID", "reward"),
                "user",
                50
            ),
            mockk()
        )

        outgoingEvents.shouldBeEmpty()
    }
}