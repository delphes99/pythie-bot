package fr.delphes.features.twitch.rewardRedeem

import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.features.handle
import fr.delphes.twitch.api.reward.Reward
import fr.delphes.twitch.api.reward.RewardConfiguration
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RewardRedeemTest {
    private val CHANNEL = "channel"
    private val reward = RewardConfiguration("reward", 100)
    private val otherReward = RewardConfiguration("other_reward", 100)

    @Test
    internal suspend fun `respond if matching reward`() {
        val response = mockk<OutgoingEvent>()

        val outgoingEvents = RewardRedeem(CHANNEL, reward) { listOf(response) }
            .handle(
                RewardRedemption(
                    Reward("featureID", reward),
                    "user",
                    50
                ),
                mockk()
            )

        assertThat(outgoingEvents).containsExactly(response)
    }

    @Test
    internal suspend fun `don't respond not matching reward`() {
        val response = mockk<OutgoingEvent>()

        val outgoingEvents = RewardRedeem(CHANNEL, otherReward) { listOf(response) }
            .handle(
                RewardRedemption(
                    Reward("featureID", reward),
                    "user",
                    50
                ),
                mockk()
            )

        assertThat(outgoingEvents).isEmpty()
    }
}