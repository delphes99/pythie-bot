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
import fr.delphes.twitch.api.reward.Reward
import fr.delphes.twitch.api.reward.RewardConfiguration
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
    private val CHANNEL = TwitchChannel("channel")
    private val reward = RewardConfiguration("voth", 100)
    private val rewardRedemption = RewardRedemption(CHANNEL, Reward("id", reward), "user", 50)
    private val NOW = LocalDateTime.of(2020, 1, 1, 0, 0)
    private val DEFAULT_STATE = VOTHState(true, VOTHWinner("oldVip", NOW.minusMinutes(5), 50))
    private val CLOCK = TestClock(NOW)
    private val CONFIGURATION = VOTHConfiguration(
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
            assertThat(voth.currentVip).isEqualTo(VOTHWinner("user", NOW, 50))
            assertThat(voth.vothChanged).isTrue()
        }

        @Test
        internal suspend fun `don't promote vip if already VOTH`() {
            val state = VOTHState(currentVip = VOTHWinner("user", NOW.minusMinutes(1), 50))
            val voth = voth(state)

            voth.handle(rewardRedemption, clientBot)
            assertThat(voth.currentVip).isEqualTo(VOTHWinner("user", NOW.minusMinutes(1), 50))
            assertThat(voth.vothChanged).isFalse()
        }

        @Test
        internal suspend fun `redeem launch vip list`() {
            val voth = voth()

            val messages = voth.handle(rewardRedemption, clientBot)
            assertThat(messages).contains(RetrieveVip(CHANNEL))
        }
    }

    @Nested
    @DisplayName("VIPListReceived")
    inner class VipListReceivedHandlers {
        @Test
        internal suspend fun `remove all old vip`() {
            val voth = voth()

            val messages = voth.handle(VIPListReceived(CHANNEL, "oldVip", "oldVip2"), clientBot)
            assertThat(messages).contains(
                RemoveVIP("oldVip", CHANNEL),
                RemoveVIP("oldVip2", CHANNEL)
            )
        }

        @Test
        internal suspend fun `promote vip`() {
            val state = VOTHState(true, VOTHWinner("newVip", NOW.minusMinutes(1), 50))
            val voth = voth(state)

            val messages = voth.handle(VIPListReceived(CHANNEL, "oldVip", "oldVip2"), clientBot)
            assertThat(messages).contains(
                PromoteVIP("newVip", CHANNEL)
            )
        }

        @Test
        internal suspend fun `do nothing when on vip change`() {
            val state = VOTHState(false, VOTHWinner("user", NOW.minusMinutes(1), 50))
            val voth = voth(state)

            val messages = voth.handle(VIPListReceived(CHANNEL, "oldVip", "oldVip2"), clientBot)
            assertThat(messages).isEmpty()
        }
    }

    @Test
    internal suspend fun `pause when stream goes offline`() {
        val state = mockk<VOTHState>(relaxed = true)
        val voth = voth(state)

        val messages = voth.handle(StreamOffline(CHANNEL), clientBot)

        verify(exactly = 1) { state.pause(any()) }
        assertThat(messages).isEmpty()
    }

    @Test
    internal suspend fun `unpause when stream goes online`() {
        val state = mockk<VOTHState>(relaxed = true)
        val voth = voth(state)

        val incomingEvent = StreamOnline(CHANNEL, "title", NOW, Game(GameId("gameId"), "game title"), "thumbnailUrl")
        val messages = voth.handle(incomingEvent, clientBot)

        verify(exactly = 1) { state.unpause(any()) }
        assertThat(messages).isEmpty()
    }

    @Test
    internal suspend fun `display top 3`() {
        val state = mockk<VOTHState>(relaxed = true)
        every { state.top3(any()) } returns listOf(Stats(User("user1")), Stats(User("user2")), Stats(User("user3")))

        val voth = VOTH(
            CHANNEL,
            VOTHConfiguration(
                reward,
                { emptyList() },
                "!cmdstats",
                { emptyList() },
                "!top3",
                { top1, top2, top3 -> listOf(SendMessage("${top1?.user?.name}, ${top2?.user?.name}, ${top3?.user?.name}", CHANNEL)) }),
            stateRepository = TestStateRepository { state },
            state = state,
            clock = CLOCK
        )

        val events = voth.handle(CommandAsked(CHANNEL, Command("!top3"), User("user")), mockk())

        assertThat(events).contains(SendMessage("user1, user2, user3", CHANNEL))
    }

    private fun voth(state: VOTHState = DEFAULT_STATE): VOTH {
        return VOTH(
            CHANNEL,
            CONFIGURATION,
            stateRepository = TestStateRepository { state },
            state = state,
            clock = CLOCK
        )
    }
}