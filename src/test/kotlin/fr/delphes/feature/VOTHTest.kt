package fr.delphes.feature

import fr.delphes.event.eventHandler.handleEvent
import fr.delphes.event.incoming.RewardRedemption
import fr.delphes.event.incoming.StreamOffline
import fr.delphes.event.incoming.StreamOnline
import fr.delphes.event.incoming.VIPListReceived
import fr.delphes.event.outgoing.PromoteVIP
import fr.delphes.event.outgoing.RemoveVIP
import fr.delphes.event.outgoing.RetrieveVip
import fr.delphes.feature.voth.VOTH
import fr.delphes.feature.voth.VOTHConfiguration
import fr.delphes.feature.voth.VOTHState
import fr.delphes.feature.voth.VOTHWinner
import fr.delphes.time.TestClock
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

    @Nested
    @DisplayName("RewardRedemption")
    inner class RewardRedemptionHandlers {
        @Test
        internal fun `promote vip`() {
            val voth = voth(VOTHState())

            voth.rewardHandlers.handleEvent(RewardRedemption(FEATURE_ID, "user", 50))
            assertThat(voth.currentVip).isEqualTo(VOTHWinner("user", NOW, 50))
            assertThat(voth.vothChanged).isTrue()
        }

        @Test
        internal fun `don't promote vip if already VOTH`() {
            val state = VOTHState(currentVip = VOTHWinner("user", NOW.minusMinutes(1), 50))
            val voth = voth(state)

            voth.rewardHandlers.handleEvent(RewardRedemption(FEATURE_ID, "user", 50))
            assertThat(voth.currentVip).isEqualTo(VOTHWinner("user", NOW.minusMinutes(1), 50))
            assertThat(voth.vothChanged).isFalse()
        }

        @Test
        internal fun `redeem launch vip list`() {
            val voth = voth()

            val messages = voth.rewardHandlers.handleEvent(RewardRedemption(FEATURE_ID, "user", 50))
            assertThat(messages).contains(RetrieveVip)
        }
    }

    @Nested
    @DisplayName("VIPListReceived")
    inner class VipListReceivedHandlers {
        @Test
        internal fun `remove all old vip`() {
            val voth = voth()

            val messages = voth.vipListReceivedHandlers.handleEvent(VIPListReceived("oldVip", "oldVip2"))
            assertThat(messages).contains(
                RemoveVIP("oldVip"),
                RemoveVIP("oldVip2")
            )
        }

        @Test
        internal fun `promote vip`() {
            val state = VOTHState(true, VOTHWinner("newVip", NOW.minusMinutes(1), 50))
            val voth = voth(state)

            val messages = voth.vipListReceivedHandlers.handleEvent(VIPListReceived("oldVip", "oldVip2"))
            assertThat(messages).contains(
                PromoteVIP("newVip")
            )
        }

        @Test
        internal fun `do nothing when on vip change`() {
            val state = VOTHState(false, VOTHWinner("user", NOW.minusMinutes(1), 50))
            val voth = voth(state)

            val messages = voth.vipListReceivedHandlers.handleEvent(VIPListReceived("oldVip", "oldVip2"))
            assertThat(messages).isEmpty()
        }
    }

    @Test
    internal fun `pause when stream goes offline`() {
        val state = mockk<VOTHState>(relaxed = true)
        val voth = voth(state)

        val messages = voth.streamOfflineHandlers.handleEvent(StreamOffline())

        verify(exactly = 1) { state.pause(any()) }
        assertThat(messages).isEmpty()
    }

    @Test
    internal fun `unpause when stream goes online`() {
        val state = mockk<VOTHState>(relaxed = true)
        val voth = voth(state)

        val messages = voth.streamOnlineHandlers.handleEvent(StreamOnline())

        verify(exactly = 1) { state.unpause(any()) }
        assertThat(messages).isEmpty()
    }

    private fun voth(state: VOTHState = DEFAULT_STATE): VOTH {
        return VOTH(CONFIGURATION, stateRepository = TestStateRepository({state}), state = state, clock = CLOCK)
    }
}