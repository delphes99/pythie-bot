package fr.delphes.feature.rewardRedeem

import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.handle
import fr.delphes.twitch.model.Feature
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RewardRedeemTest {
    @Test
    internal suspend fun `respond if matching feature id`() {
        val response = mockk<OutgoingEvent>()

        val outgoingEvents = RewardRedeem("featureID") { listOf(response) }
            .handle(
                RewardRedemption(
                    Feature("featureID", "featureName"),
                    "user",
                    50
                ),
                mockk()
            )

        assertThat(outgoingEvents).containsExactly(response)
    }

    @Test
    internal suspend fun `respond if matching feature name`() {
        val response = mockk<OutgoingEvent>()

        val outgoingEvents = RewardRedeem("featureName") { listOf(response) }
            .handle(
                RewardRedemption(
                    Feature("featureID", "featureName"),
                    "user",
                    50
                ),
                mockk()
            )

        assertThat(outgoingEvents).containsExactly(response)
    }

    @Test
    internal suspend fun `don't respond not matching feature`() {
        val response = mockk<OutgoingEvent>()

        val outgoingEvents = RewardRedeem("otherFeatureName") { listOf(response) }
            .handle(
                RewardRedemption(
                    Feature("featureID", "featureName"),
                    "user",
                    50
                ),
                mockk()
            )

        assertThat(outgoingEvents).isEmpty()
    }
}