package fr.delphes.features.twitch.rewardRedeem

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.RewardId
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class RewardRedeemTest {
    private val channel = TwitchChannel("channel")
    private val reward = RewardConfiguration("reward", 100)
    private val otherReward = RewardConfiguration("other_reward", 100)

    @Test
    internal fun `respond if matching reward`() {
        val outgoingEvent = mockk<OutgoingEvent>()

        val outgoingEvents = runBlocking {
            RewardRedeem(channel, reward) { listOf(outgoingEvent) }.handleIncomingEvent(
                RewardRedemption(
                    channel,
                    RewardId("featureID", "reward"),
                    "user",
                    50
                ),
                mockk()
            )
        }

        outgoingEvents.shouldContain(outgoingEvent)
    }

    @Test
    internal fun `don't respond not matching reward`() {
        val outgoingEvents = runBlocking {
            RewardRedeem(channel, otherReward) { listOf(mockk()) }.handleIncomingEvent(
                RewardRedemption(
                    channel,
                    RewardId("featureID", "reward"),
                    "user",
                    50
                ),
                mockk()
            )
        }

        outgoingEvents.shouldBeEmpty()
    }
}