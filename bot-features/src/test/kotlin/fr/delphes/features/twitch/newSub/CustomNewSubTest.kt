package fr.delphes.features.twitch.newSub

import fr.delphes.connector.twitch.incomingEvent.NewSub
import fr.delphes.features.hasReceived
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class CustomNewSubTest : ShouldSpec({
    should("execute action if channel match") {
        var isCalled = false

        val customNewSub = CustomNewSub(CHANNEL) {
            isCalled = true
        }

        customNewSub.hasReceived(
            NewSub(
                CHANNEL,
                UserName("user")
            )
        )

        isCalled shouldBe true
    }
    should("not execute action if channel doesn't match") {
        var isCalled = false

        val customNewSub = CustomNewSub(CHANNEL) {
            isCalled = true
        }

        customNewSub.hasReceived(
            NewSub(
                TwitchChannel("otherchannel"),
                UserName("user")
            )
        )

        isCalled shouldBe false
    }

}) {
    companion object {
        private val CHANNEL = TwitchChannel("channel")
    }
}