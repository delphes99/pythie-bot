package fr.delphes.feature

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.bot.event.incoming.StreamOffline
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.event.incoming.VIPListReceived
import fr.delphes.bot.event.outgoing.PromoteVIP
import fr.delphes.bot.event.outgoing.RemoveVIP
import fr.delphes.bot.event.outgoing.RetrieveVip
import fr.delphes.feature.voth.VOTH
import fr.delphes.feature.voth.VOTHConfiguration
import fr.delphes.feature.voth.VOTHState
import fr.delphes.feature.voth.VOTHWinner
import fr.delphes.bot.time.TestClock
import fr.delphes.bot.twitch.game.Game
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class VOTHTest {
    val FEATURE_ID = "featureID"
    val NOW = LocalDateTime.of(2020, 1, 1, 0, 0)
    val DEFAULT_STATE = VOTHState(true, VOTHWinner("oldVip", NOW.minusMinutes(5), 50))
    val CLOCK = TestClock(NOW)
    val CONFIGURATION = VOTHConfiguration(FEATURE_ID, { emptyList() }, "!cmdstats", { emptyList() })
    val channelInfo = mockk<ChannelInfo>()

    @Nested
    @DisplayName("RewardRedemption")
    inner class RewardRedemptionHandlers {
        @Test
        internal fun `promote vip`() {
            val voth = voth(VOTHState())

            voth.VOTHRewardRedemptionHandler().handle(RewardRedemption(FEATURE_ID, "user", 50), channelInfo)
            assertThat(voth.currentVip).isEqualTo(VOTHWinner("user", NOW, 50))
            assertThat(voth.vothChanged).isTrue()
        }

        @Test
        internal fun `don't promote vip if already VOTH`() {
            val state = VOTHState(currentVip = VOTHWinner("user", NOW.minusMinutes(1), 50))
            val voth = voth(state)

            voth.VOTHRewardRedemptionHandler().handle(RewardRedemption(FEATURE_ID, "user", 50), channelInfo)
            assertThat(voth.currentVip).isEqualTo(VOTHWinner("user", NOW.minusMinutes(1), 50))
            assertThat(voth.vothChanged).isFalse()
        }

        @Test
        internal fun `redeem launch vip list`() {
            val voth = voth()

            val messages = voth.VOTHRewardRedemptionHandler().handle(RewardRedemption(FEATURE_ID, "user", 50), channelInfo)
            assertThat(messages).contains(RetrieveVip)
        }
    }

    @Nested
    @DisplayName("VIPListReceived")
    inner class VipListReceivedHandlers {
        @Test
        internal fun `remove all old vip`() {
            val voth = voth()

            val messages = voth.VOTHVIPListReceivedHandler().handle(VIPListReceived("oldVip", "oldVip2"), channelInfo)
            assertThat(messages).contains(
                RemoveVIP("oldVip"),
                RemoveVIP("oldVip2")
            )
        }

        @Test
        internal fun `promote vip`() {
            val state = VOTHState(true, VOTHWinner("newVip", NOW.minusMinutes(1), 50))
            val voth = voth(state)

            val messages = voth.VOTHVIPListReceivedHandler().handle(VIPListReceived("oldVip", "oldVip2"), channelInfo)
            assertThat(messages).contains(
                PromoteVIP("newVip")
            )
        }

        @Test
        internal fun `do nothing when on vip change`() {
            val state = VOTHState(false, VOTHWinner("user", NOW.minusMinutes(1), 50))
            val voth = voth(state)

            val messages = voth.VOTHVIPListReceivedHandler().handle(VIPListReceived("oldVip", "oldVip2"), channelInfo)
            assertThat(messages).isEmpty()
        }
    }

    @Test
    internal fun `pause when stream goes offline`() {
        val state = mockk<VOTHState>(relaxed = true)
        val voth = voth(state)

        val messages = voth.StreamOfflineHandler().handle(StreamOffline, channelInfo)

        verify(exactly = 1) { state.pause(any()) }
        assertThat(messages).isEmpty()
    }

    @Test
    internal fun `unpause when stream goes online`() {
        val state = mockk<VOTHState>(relaxed = true)
        val voth = voth(state)

        val messages = voth.StreamOnlineHandler().handle(StreamOnline("title", NOW, Game("gameId", "game title")), channelInfo)

        verify(exactly = 1) { state.unpause(any()) }
        assertThat(messages).isEmpty()
    }

    private fun voth(state: VOTHState = DEFAULT_STATE): VOTH {
        return VOTH(CONFIGURATION, stateRepository = TestStateRepository({state}), state = state, clock = CLOCK)
    }
}