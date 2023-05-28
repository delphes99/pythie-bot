package fr.delphes.features.twitch.voth

import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.connector.twitch.outgoingEvent.PromoteVIP
import fr.delphes.features.TestEventHandlerAction
import fr.delphes.features.testRuntime
import fr.delphes.state.state.ClockState
import fr.delphes.test.TestClock
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.RewardId
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import java.time.LocalDateTime

class VOTHTest : ShouldSpec({
    context("RewardRedemption") {
        should("promote vip") {
            val executionContext = VOTH(
                channel,
                reward,
                TestEventHandlerAction()
            ).testRuntime {
                atFixedTime(NOW)
                withState(VOTHState(channel, clock = ClockState(TestClock(NOW))))
            }.hasReceived(rewardRedemption)

            executionContext.shouldState(VOTHState.idFor(channel)) {
                currentVip shouldBe VOTHWinner("user", NOW, 50)
            }
        }
        
        should("announce new vip") {
            val newVipAnnouncer = TestEventHandlerAction<NewVOTHAnnounced>()
            VOTH(
                channel,
                reward,
                newVipAnnouncer
            ).testRuntime {
                atFixedTime(NOW)
                withState(VOTHState(channel, clock = ClockState(TestClock(NOW))))
            }.hasReceived(rewardRedemption)

            newVipAnnouncer.shouldHaveBeenCalled()
        }

        should("don't promote vip if already VOTH") {
            val executionContext = VOTH(
                channel,
                reward,
                TestEventHandlerAction()
            ).testRuntime {
                atFixedTime(NOW)
                withState(VOTHState(channel, clock = ClockState(TestClock(NOW))).apply {
                    state = VOTHStateData(currentVip = VOTHWinner("user", NOW.minusMinutes(1), 50))
                })
            }.hasReceived(rewardRedemption)

            coVerify(exactly = 0) { executionContext.bot.processOutgoingEvent(any<PromoteVIP>()) }
        }

        should("don't announce vip if already VOTH") {
            val newVipAnnouncer = TestEventHandlerAction<NewVOTHAnnounced>()
            VOTH(
                channel,
                reward,
                newVipAnnouncer
            ).testRuntime {
                atFixedTime(NOW)
                withState(VOTHState(channel, clock = ClockState(TestClock(NOW))).apply {
                    state = VOTHStateData(currentVip = VOTHWinner("user", NOW.minusMinutes(1), 50))
                })
            }.hasReceived(rewardRedemption)

            newVipAnnouncer.shouldNotHaveBeenCalled()
        }
    }

    should("pause when stream goes offline") {
        val executionContext = VOTH(
            channel,
            reward,
            TestEventHandlerAction()
        ).testRuntime {
            atFixedTime(NOW)
            withMockedState(VOTHState.idFor(channel))
        }.hasReceived(StreamOffline(channel))

        executionContext.shouldState(VOTHState.idFor(channel)) {
            coVerify(exactly = 1) { this@shouldState.pause() }
        }
    }

    should("unpause when stream goes online") {
        val executionContext = VOTH(
            channel,
            reward,
            TestEventHandlerAction()
        ).testRuntime {
            atFixedTime(NOW)
            withMockedState(VOTHState.idFor(channel))
        }.hasReceived(StreamOnline(channel, "id", "title", NOW, Game(GameId("gameId"), "game title"), "thumbnailUrl"))

        executionContext.shouldState(VOTHState.idFor(channel)) {
            coVerify(exactly = 1) { this@shouldState.unpause() }
        }
    }
}) {
    companion object {
        private val channel = TwitchChannel("channel")
        private const val rewardName = "voth"
        private val reward = RewardConfiguration(rewardName, 100)
        private val rewardRedemption = RewardRedemption(channel, RewardId("id", rewardName), "user", 50)
        private val NOW = LocalDateTime.of(2020, 1, 1, 0, 0)
    }
}