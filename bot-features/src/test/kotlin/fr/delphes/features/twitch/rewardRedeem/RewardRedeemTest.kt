package fr.delphes.features.twitch.rewardRedeem

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.RewardId
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.mockk.mockk

class RewardRedeemTest : ShouldSpec({
    should("respond if matching reward") {
        val outgoingEvent = mockk<OutgoingEvent>()

        val outgoingEvents = RewardRedeem(channel, reward) { listOf(outgoingEvent) }.handleIncomingEvent(
            RewardRedemption(
                channel,
                RewardId("featureID", "reward"),
                "user",
                50
            ),
            mockk()
        )

        outgoingEvents.shouldContain(outgoingEvent)
    }

    should("don't respond not matching reward") {
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
}) {
    companion object {
        private val channel = TwitchChannel("channel")
        private val reward = RewardConfiguration("reward", 100)
        private val otherReward = RewardConfiguration("other_reward", 100)
    }
}