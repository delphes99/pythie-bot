package fr.delphes.features.twitch.rewardRedeem

import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.connector.twitch.reward.RewardTitle
import fr.delphes.features.TestIncomingEventHandlerAction
import fr.delphes.features.testRuntime
import fr.delphes.twitch.TwitchChannel
import io.kotest.core.spec.style.ShouldSpec

class RewardRedeemTest : ShouldSpec({
    should("respond if matching reward") {
        val testEventHandler = TestIncomingEventHandlerAction<RewardRedemption>()

        val customNewSub = RewardRedeem(CHANNEL, REWARD, action = testEventHandler)

        customNewSub.testRuntime().hasReceived(
            RewardRedemption(
                REWARD,
                "user",
                50
            )
        )

        testEventHandler.shouldHaveBeenCalled()
    }
    should("don't respond not matching reward") {
        val testEventHandler = TestIncomingEventHandlerAction<RewardRedemption>()

        val customNewSub = RewardRedeem(CHANNEL, REWARD, action = testEventHandler)

        customNewSub.testRuntime().hasReceived(
            RewardRedemption(
                RewardId(CHANNEL, RewardTitle("other reward")),
                "user",
                50
            )
        )

        testEventHandler.shouldNotHaveBeenCalled()
    }
}) {
    companion object {
        private val CHANNEL = TwitchChannel("channel")
        private val REWARD = RewardId(CHANNEL, RewardTitle("reward"))
    }
}