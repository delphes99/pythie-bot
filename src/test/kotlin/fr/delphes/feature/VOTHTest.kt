package fr.delphes.feature

import fr.delphes.event.eventHandler.handleEvent
import fr.delphes.event.incoming.RewardRedemption
import fr.delphes.event.incoming.VIPListReceived
import fr.delphes.event.outgoing.PromoteVIP
import fr.delphes.event.outgoing.RemoveVIP
import fr.delphes.event.outgoing.RetrieveVip
import fr.delphes.time.TestClock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class VOTHTest {
    val featureID = "featureID"
    val now = LocalDateTime.of(2020, 1, 1, 0, 0)
    val clock = TestClock(now)

    @Nested
    @DisplayName("RewardRedemption")
    inner class RewardRedemptionHandlers {
        @Test
        internal fun `promote vip`() {
            val voth = VOTH(featureID, clock) { "response" }
            voth.rewardHandlers.handleEvent(RewardRedemption(featureID, "user"))
            assertThat(voth.currentVip).isEqualTo(VOTHWinner("user", now))
            assertThat(voth.vothChanged).isTrue()
        }

        @Test
        internal fun `don't promote vip if already VOTH`() {
            val voth = VOTH(featureID, clock) { "response" }
            voth.currentVip = VOTHWinner("user", now.minusMinutes(1))

            voth.rewardHandlers.handleEvent(RewardRedemption(featureID, "user"))
            assertThat(voth.currentVip).isEqualTo(VOTHWinner("user", now.minusMinutes(1)))
            assertThat(voth.vothChanged).isFalse()
        }

        @Test
        internal fun `redeem launch vip list`() {
            val voth = voth()
            val messages = voth.rewardHandlers.handleEvent(RewardRedemption(featureID, "user"))
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
            val voth = voth()
            voth.currentVip = VOTHWinner("newVip", now.minusMinutes(1))
            val messages = voth.vipListReceivedHandlers.handleEvent(VIPListReceived("oldVip", "oldVip2"))
            assertThat(messages).contains(
                PromoteVIP("newVip")
            )
        }

        @Test
        internal fun `do nothing when on vip change`() {
            val voth = VOTH(featureID, clock) { "response" }
            voth.currentVip = VOTHWinner("user", now.minusMinutes(1))
            voth.vothChanged = false
            val messages = voth.vipListReceivedHandlers.handleEvent(VIPListReceived("oldVip", "oldVip2"))
            assertThat(messages).isEmpty()
        }
    }

    private fun voth(): VOTH {
        val voth = VOTH(featureID, clock) { "response" }
        voth.vothChanged = true
        voth.currentVip = VOTHWinner("oldVip", now.minusMinutes(5))
        return voth
    }
}