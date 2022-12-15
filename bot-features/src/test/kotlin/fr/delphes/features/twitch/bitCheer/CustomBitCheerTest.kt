package fr.delphes.features.twitch.bitCheer

import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.features.hasReceived
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class CustomBitCheerTest : ShouldSpec({
    should("execute action if channel match") {
        var isCalled = false

        val customNewFollow = CustomBitCheer(CHANNEL) {
            isCalled = true
        }

        customNewFollow.hasReceived(
            BitCheered(
                CHANNEL,
                User("user"),
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

        customNewFollow.hasReceived(
            BitCheered(
                TwitchChannel("otherchannel"),
                User("user"),
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