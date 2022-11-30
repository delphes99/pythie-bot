package fr.delphes.features.twitch.voth

import fr.delphes.test.TestClock
import fr.delphes.bot.Bot
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.connector.twitch.incomingEvent.VIPListReceived
import fr.delphes.connector.twitch.outgoingEvent.PromoteVIP
import fr.delphes.connector.twitch.outgoingEvent.RemoveVIP
import fr.delphes.connector.twitch.outgoingEvent.RetrieveVip
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.features.TestStateRepository
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.RewardId
import fr.delphes.twitch.api.user.User
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

class VOTHTest : ShouldSpec({
    val clientBot = mockk<Bot>()

    context("RewardRedemption") {
        should("promote vip") {
            val voth = voth(VOTHState())

            voth.handleIncomingEvent(rewardRedemption, clientBot)

            voth.currentVip shouldBe VOTHWinner("user", now, 50)
            voth.vothChanged.shouldBeTrue()
        }

        should("don't promote vip if already VOTH") {
            val state = VOTHState(currentVip = VOTHWinner("user", now.minusMinutes(1), 50))
            val voth = voth(state)

            voth.handleIncomingEvent(rewardRedemption, clientBot)

            voth.currentVip shouldBe VOTHWinner("user", now.minusMinutes(1), 50)
            voth.vothChanged.shouldBeFalse()
        }

        should("redeem launch vip list") {
            val voth = voth()

            val messages = voth.handleIncomingEvent(rewardRedemption, clientBot)

            messages.shouldContain(RetrieveVip(channel))
        }
    }

    context("VIPListReceived") {
        should("remove all old vip") {
            val voth = voth()

            val messages = voth.handleIncomingEvent(VIPListReceived(channel, "oldVip", "oldVip2"), clientBot)

            messages.shouldContainAll(
                RemoveVIP("oldVip", channel),
                RemoveVIP("oldVip2", channel)
            )
        }

        should("promote vip") {
            val state = VOTHState(true, VOTHWinner("newVip", now.minusMinutes(1), 50))
            val voth = voth(state)

            val messages = voth.handleIncomingEvent(VIPListReceived(channel, "oldVip", "oldVip2"), clientBot)

            messages.shouldContain(
                PromoteVIP("newVip", channel)
            )
        }

        should("do nothing when on vip change") {
            val state = VOTHState(false, VOTHWinner("user", now.minusMinutes(1), 50))
            val voth = voth(state)

            val messages = voth.handleIncomingEvent(VIPListReceived(channel, "oldVip", "oldVip2"), clientBot)

            messages.shouldBeEmpty()
        }
    }

    should("pause when stream goes offline") {
        val state = mockk<VOTHState>(relaxed = true)
        val voth = voth(state)

        val messages = voth.handleIncomingEvent(StreamOffline(channel), clientBot)

        verify(exactly = 1) { state.pause(any()) }
        messages.shouldBeEmpty()
    }

    should("unpause when stream goes online") {
        val state = mockk<VOTHState>(relaxed = true)
        val voth = voth(state)

        val incomingEvent = StreamOnline(channel, "id", "title", now, Game(GameId("gameId"), "game title"), "thumbnailUrl")
        val messages = voth.handleIncomingEvent(incomingEvent, clientBot)

        verify(exactly = 1) { state.unpause(any()) }
        messages.shouldBeEmpty()
    }

    should("display top 3") {
        val state = mockk<VOTHState>(relaxed = true)
        every { state.top3(any()) } returns listOf(Stats(User("user1")), Stats(User("user2")), Stats(User("user3")))

        val voth = VOTH(
            channel,
            VOTHConfiguration(
                reward,
                { emptyList() },
                "!cmdstats",
                { emptyList() },
                "!top3",
                { top1, top2, top3 -> listOf(SendMessage("${top1?.user?.name}, ${top2?.user?.name}, ${top3?.user?.name}", channel)) }),
            stateRepository = TestStateRepository { state },
            state = state,
            clock = clock
        )
        val bot = mockk<Bot>()
        every { bot.connectors } returns listOf(mockk<TwitchConnector>())

        val events = voth.handleIncomingEvent(CommandAsked(channel, Command("!top3"), User("user")), bot)

        events.shouldContain(SendMessage("user1, user2, user3", channel))
    }
}) {
    companion object {
        private val channel = TwitchChannel("channel")
        private const val rewardName = "voth"
        private val reward = RewardConfiguration(rewardName, 100)
        private val rewardRedemption = RewardRedemption(channel, RewardId("id", rewardName), "user", 50)
        private val now = LocalDateTime.of(2020, 1, 1, 0, 0)
        private val defaultState = VOTHState(true, VOTHWinner("oldVip", now.minusMinutes(5), 50))
        private val clock = TestClock(now)
        private val configuration = VOTHConfiguration(
            reward,
            { emptyList() },
            "!cmdstats",
            { emptyList() },
            "!top3",
            { _, _, _ -> emptyList() })

        private fun voth(state: VOTHState = defaultState): VOTH {
            return VOTH(
                channel,
                configuration,
                stateRepository = TestStateRepository { state },
                state = state,
                clock = clock
            )
        }
    }
}