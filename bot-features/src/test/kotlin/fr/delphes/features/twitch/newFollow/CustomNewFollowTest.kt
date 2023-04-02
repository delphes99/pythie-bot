package fr.delphes.features.twitch.newFollow

import fr.delphes.connector.twitch.incomingEvent.NewFollow
import fr.delphes.features.hasReceived
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class CustomNewFollowTest : ShouldSpec({
    should("execute action if channel match") {
        var isCalled = false

        val customNewFollow = CustomNewFollow(CHANNEL) {
            isCalled = true
        }

        customNewFollow.hasReceived(
            NewFollow(
                CHANNEL,
                UserName("user")
            )
        )

        isCalled shouldBe true
    }
    should("not execute action if channel doesn't match") {
        var isCalled = false

        val customNewFollow = CustomNewFollow(CHANNEL) {
            isCalled = true
        }

        customNewFollow.hasReceived(
            NewFollow(
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
