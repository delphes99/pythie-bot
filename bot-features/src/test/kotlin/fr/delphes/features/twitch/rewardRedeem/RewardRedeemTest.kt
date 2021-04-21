package fr.delphes.features.twitch.rewardRedeem

import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.RewardId
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RewardRedeemTest {
    private val CHANNEL = TwitchChannel("channel")
    private val reward = RewardConfiguration("reward", 100)
    private val otherReward = RewardConfiguration("other_reward", 100)

    @Test
    internal suspend fun `respond if matching reward`() {
        val outgoingEvents = RewardRedeem(CHANNEL, reward) { listOf(mockk()) }.handleIncomingEvent(
            RewardRedemption(
                CHANNEL,
                RewardId("featureID", "reward"),
                "user",
                50
            ),
            mockk()
        )

        assertThat(outgoingEvents).containsExactly(mockk())
    }

    @Test
    internal suspend fun `don't respond not matching reward`() {

        val outgoingEvents = RewardRedeem(CHANNEL, otherReward) { listOf(mockk()) }.handleIncomingEvent(
            RewardRedemption(
                CHANNEL,
                RewardId("featureID", "reward"),
                "user",
                50
            ),
            mockk()
        )

        assertThat(outgoingEvents).isEmpty()
    }
}