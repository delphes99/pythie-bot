package fr.delphes.features.twitch.voth

import fr.delphes.bot.Bot
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
import fr.delphes.features.TestClock
import fr.delphes.features.TestStateRepository
import fr.delphes.features.handle
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.RewardId
import fr.delphes.twitch.api.user.User
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class VOTHTest {
    private val channel = TwitchChannel("channel")
    private val rewardName = "voth"
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
    val clientBot = mockk<Bot>()

    @Nested
    @DisplayName("RewardRedemption")
    inner class RewardRedemptionHandlers {
        @Test
        internal suspend fun `promote vip`() {
            val voth = voth(VOTHState())

            voth.handle(rewardRedemption, clientBot)
            assertThat(voth.currentVip).isEqualTo(VOTHWinner("user", now, 50))
            assertThat(voth.vothChanged).isTrue()
        }

        @Test
        internal suspend fun `don't promote vip if already VOTH`() {
            val state = VOTHState(currentVip = VOTHWinner("user", now.minusMinutes(1), 50))
            val voth = voth(state)

            voth.handle(rewardRedemption, clientBot)
            assertThat(voth.currentVip).isEqualTo(VOTHWinner("user", now.minusMinutes(1), 50))
            assertThat(voth.vothChanged).isFalse()
        }

        @Test
        internal suspend fun `redeem launch vip list`() {
            val voth = voth()

            val messages = voth.handle(rewardRedemption, clientBot)
            assertThat(messages).contains(RetrieveVip(channel))
        }
    }

    @Nested
    @DisplayName("VIPListReceived")
    inner class VipListReceivedHandlers {
        @Test
        internal suspend fun `remove all old vip`() {
            val voth = voth()

            val messages = voth.handle(VIPListReceived(channel, "oldVip", "oldVip2"), clientBot)
            assertThat(messages).contains(
                RemoveVIP("oldVip", channel),
                RemoveVIP("oldVip2", channel)
            )
        }

        @Test
        internal suspend fun `promote vip`() {
            val state = VOTHState(true, VOTHWinner("newVip", now.minusMinutes(1), 50))
            val voth = voth(state)

            val messages = voth.handle(VIPListReceived(channel, "oldVip", "oldVip2"), clientBot)
            assertThat(messages).contains(
                PromoteVIP("newVip", channel)
            )
        }

        @Test
        internal suspend fun `do nothing when on vip change`() {
            val state = VOTHState(false, VOTHWinner("user", now.minusMinutes(1), 50))
            val voth = voth(state)

            val messages = voth.handle(VIPListReceived(channel, "oldVip", "oldVip2"), clientBot)
            assertThat(messages).isEmpty()
        }
    }

    @Test
    internal suspend fun `pause when stream goes offline`() {
        val state = mockk<VOTHState>(relaxed = true)
        val voth = voth(state)

        val messages = voth.handle(StreamOffline(channel), clientBot)

        verify(exactly = 1) { state.pause(any()) }
        assertThat(messages).isEmpty()
    }

    @Test
    internal suspend fun `unpause when stream goes online`() {
        val state = mockk<VOTHState>(relaxed = true)
        val voth = voth(state)

        val incomingEvent = StreamOnline(channel, "title", now, Game(GameId("gameId"), "game title"), "thumbnailUrl")
        val messages = voth.handle(incomingEvent, clientBot)

        verify(exactly = 1) { state.unpause(any()) }
        assertThat(messages).isEmpty()
    }

    @Test
    internal suspend fun `display top 3`() {
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

        val events = voth.handle(CommandAsked(channel, Command("!top3"), User("user")), mockk())

        assertThat(events).contains(SendMessage("user1, user2, user3", channel))
    }

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