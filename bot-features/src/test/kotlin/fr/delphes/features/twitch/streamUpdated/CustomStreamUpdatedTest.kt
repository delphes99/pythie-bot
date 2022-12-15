package fr.delphes.features.twitch.streamUpdated

import fr.delphes.connector.twitch.incomingEvent.StreamChanged
import fr.delphes.features.hasReceived
import fr.delphes.twitch.TwitchChannel
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class CustomStreamUpdatedTest : ShouldSpec({
    should("execute action if channel match") {
        var isCalled = false

        val customStreamUpdated = CustomStreamUpdated(CHANNEL) {
            isCalled = true
        }

        customStreamUpdated.hasReceived(
            StreamChanged(
                CHANNEL
            )
        )

        isCalled shouldBe true
    }
    should("not execute action if channel doesn't match") {
        var isCalled = false

        val customStreamUpdated = CustomStreamUpdated(CHANNEL) {
            isCalled = true
        }

        customStreamUpdated.hasReceived(
            StreamChanged(
                TwitchChannel("otherchannel")
            )
        )

        isCalled shouldBe false
    }

}) {
    companion object {
        private val CHANNEL = TwitchChannel("channel")
    }
}
