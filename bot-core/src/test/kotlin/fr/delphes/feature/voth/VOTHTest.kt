package fr.delphes.feature.voth

import fr.delphes.twitch.model.User
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.command.Command
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.bot.event.incoming.StreamOffline
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.event.incoming.VIPListReceived
import fr.delphes.bot.event.outgoing.PromoteVIP
import fr.delphes.bot.event.outgoing.RemoveVIP
import fr.delphes.bot.event.outgoing.RetrieveVip
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.bot.util.time.TestClock
import fr.delphes.feature.TestStateRepository
import fr.delphes.feature.handle
import fr.delphes.twitch.model.Feature
import fr.delphes.twitch.model.Game
import fr.delphes.twitch.model.SimpleGameId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class VOTHTest {
    val FEATURE_ID = "featureID"
    val FEATURE = Feature(FEATURE_ID, "feature")
    val NOW = LocalDateTime.of(2020, 1, 1, 0, 0)
    val DEFAULT_STATE = VOTHState(true, VOTHWinner("oldVip", NOW.minusMinutes(5), 50))
    val CLOCK = TestClock(NOW)
    val CONFIGURATION = VOTHConfiguration(
        FEATURE_ID,
        { emptyList() },
        "!cmdstats",
        { emptyList() },
        "!top3",
        { _, _, _ -> emptyList() })
    val channelInfo = mockk<ChannelInfo>()

    @Nested
    @DisplayName("RewardRedemption")
    inner class RewardRedemptionHandlers {
        @Test
        internal suspend fun `promote vip`() {
            val voth = voth(VOTHState())

            voth.handle(RewardRedemption(FEATURE, "user", 50), channelInfo)
            assertThat(voth.currentVip).isEqualTo(VOTHWinner("user", NOW, 50))
            assertThat(voth.vothChanged).isTrue()
        }

        @Test
        internal suspend fun `don't promote vip if already VOTH`() {
            val state = VOTHState(currentVip = VOTHWinner("user", NOW.minusMinutes(1), 50))
            val voth = voth(state)

            voth.handle(RewardRedemption(FEATURE, "user", 50), channelInfo)
            assertThat(voth.currentVip).isEqualTo(VOTHWinner("user", NOW.minusMinutes(1), 50))
            assertThat(voth.vothChanged).isFalse()
        }

        @Test
        internal suspend fun `redeem launch vip list`() {
            val voth = voth()

            val messages = voth.handle(RewardRedemption(FEATURE, "user", 50), channelInfo)
            assertThat(messages).contains(RetrieveVip)
        }
    }

    @Nested
    @DisplayName("VIPListReceived")
    inner class VipListReceivedHandlers {
        @Test
        internal suspend fun `remove all old vip`() {
            val voth = voth()

            val messages = voth.handle(VIPListReceived("oldVip", "oldVip2"), channelInfo)
            assertThat(messages).contains(
                RemoveVIP("oldVip"),
                RemoveVIP("oldVip2")
            )
        }

        @Test
        internal suspend fun `promote vip`() {
            val state = VOTHState(true, VOTHWinner("newVip", NOW.minusMinutes(1), 50))
            val voth = voth(state)

            val messages = voth.handle(VIPListReceived("oldVip", "oldVip2"), channelInfo)
            assertThat(messages).contains(
                PromoteVIP("newVip")
            )
        }

        @Test
        internal suspend fun `do nothing when on vip change`() {
            val state = VOTHState(false, VOTHWinner("user", NOW.minusMinutes(1), 50))
            val voth = voth(state)

            val messages = voth.handle(VIPListReceived("oldVip", "oldVip2"), channelInfo)
            assertThat(messages).isEmpty()
        }
    }

    @Test
    internal suspend fun `pause when stream goes offline`() {
        val state = mockk<VOTHState>(relaxed = true)
        val voth = voth(state)

        val messages = voth.handle(StreamOffline, channelInfo)

        verify(exactly = 1) { state.pause(any()) }
        assertThat(messages).isEmpty()
    }

    @Test
    internal suspend fun `unpause when stream goes online`() {
        val state = mockk<VOTHState>(relaxed = true)
        val voth = voth(state)

        val incomingEvent = StreamOnline("title", NOW, Game(SimpleGameId("gameId"), "game title"))
        val messages = voth.handle(incomingEvent, channelInfo)

        verify(exactly = 1) { state.unpause(any()) }
        assertThat(messages).isEmpty()
    }

    @Test
    internal suspend fun `display top 3`() {
        val state = mockk<VOTHState>(relaxed = true)
        every { state.top3(any()) } returns listOf(Stats(User("user1")), Stats(User("user2")), Stats(User("user3")))

        val voth = VOTH(
            VOTHConfiguration(
                FEATURE_ID,
                { emptyList() },
                "!cmdstats",
                { emptyList() },
                "!top3",
                { top1, top2, top3 -> listOf(SendMessage("${top1?.user?.name}, ${top2?.user?.name}, ${top3?.user?.name}")) }),
            stateRepository = TestStateRepository({ state }),
            state = state,
            clock = CLOCK
        )

        val events = voth.handle(CommandAsked(Command("!top3"), User("user")), mockk())

        assertThat(events).contains(SendMessage("user1, user2, user3"))
    }

    private fun voth(state: VOTHState = DEFAULT_STATE): VOTH {
        return VOTH(CONFIGURATION, stateRepository = TestStateRepository({ state }), state = state, clock = CLOCK)
    }
}