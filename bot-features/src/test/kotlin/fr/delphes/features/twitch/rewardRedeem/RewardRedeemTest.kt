package fr.delphes.features.twitch.rewardRedeem

import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.features.TestEventHandlerAction
import fr.delphes.features.testRuntime
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.RewardId
import io.kotest.core.spec.style.ShouldSpec

class RewardRedeemTest : ShouldSpec({
    should("respond if matching reward") {
        val testEventHandler = TestEventHandlerAction<RewardRedemption>()

        val customNewSub = RewardRedeem(CHANNEL, REWARD, action = testEventHandler)

        customNewSub.testRuntime().hasReceived(
            RewardRedemption(
                CHANNEL,
                RewardId("featureID", "reward"),
                "user",
                50
            )
        )

        testEventHandler.shouldHaveBeenCalled()
    }
    should("don't respond not matching reward") {
        val testEventHandler = TestEventHandlerAction<RewardRedemption>()

        val customNewSub = RewardRedeem(CHANNEL, REWARD, action = testEventHandler)

        customNewSub.testRuntime().hasReceived(
            RewardRedemption(
                CHANNEL,
                RewardId("otherFeatureID", "other reward"),
                "user",
                50
            )
        )

        testEventHandler.shouldNotHaveBeenCalled()
    }
}) {
    companion object {
        private val CHANNEL = TwitchChannel("channel")
        private val REWARD = RewardConfiguration("reward", 100)
    }
}