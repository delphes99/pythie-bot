package fr.delphes.features.twitch.streamOffline

import fr.delphes.features.hasReceived
import fr.delphes.twitch.TwitchChannel
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import fr.delphes.connector.twitch.incomingEvent.StreamOffline as StreamOfflineEvent

class CustomStreamOfflineTest : ShouldSpec({
    should("execute action if channel match") {
        var isCalled = false

        val customStreamOffline = CustomStreamOffline(CHANNEL) {
            isCalled = true
        }

        customStreamOffline.hasReceived(
            StreamOfflineEvent(
                CHANNEL
            )
        )

        isCalled shouldBe true
    }
    should("not execute action if channel doesn't match") {
        var isCalled = false

        val customStreamOffline = CustomStreamOffline(CHANNEL) {
            isCalled = true
        }

        customStreamOffline.hasReceived(
            StreamOfflineEvent(
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
