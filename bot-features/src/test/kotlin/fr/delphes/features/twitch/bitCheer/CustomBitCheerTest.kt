package fr.delphes.features.twitch.bitCheer

import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.features.testRuntime
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class CustomBitCheerTest : ShouldSpec({
    should("execute action if channel match") {
        var isCalled = false

        val customNewFollow = CustomBitCheer(CHANNEL) {
            isCalled = true
        }

        customNewFollow.testRuntime().hasReceived(
            BitCheered(
                CHANNEL,
                UserName("user"),
                100L,
                "message"
            )
        )

        isCalled shouldBe true
    }
    should("not execute action if channel doesn't match") {
        var isCalled = false

        val customNewFollow = CustomBitCheer(CHANNEL) {
            isCalled = true
        }

        customNewFollow.testRuntime().hasReceived(
            BitCheered(
                TwitchChannel("otherchannel"),
                UserName("user"),
                100L,
                "message"
            )
        )

        isCalled shouldBe false
    }

}) {
    companion object {
        private val CHANNEL = TwitchChannel("channel")
    }
}