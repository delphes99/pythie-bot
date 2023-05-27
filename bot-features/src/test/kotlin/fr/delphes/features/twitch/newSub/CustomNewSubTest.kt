package fr.delphes.features.twitch.newSub

import fr.delphes.connector.twitch.incomingEvent.NewSub
import fr.delphes.features.TestEventHandlerAction
import fr.delphes.features.testRuntime
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import io.kotest.core.spec.style.ShouldSpec

class CustomNewSubTest : ShouldSpec({
    should("execute action if channel match") {
        val testEventHandler = TestEventHandlerAction<NewSub>()

        val customNewSub = CustomNewSub(CHANNEL, action = testEventHandler)

        customNewSub.testRuntime().hasReceived(
            NewSub(
                CHANNEL,
                UserName("user")
            )
        )

        testEventHandler.shouldHaveBeenCalled()
    }
    should("not execute action if channel doesn't match") {
        val testEventHandler = TestEventHandlerAction<NewSub>()

        val customNewSub = CustomNewSub(CHANNEL, action = testEventHandler)

        customNewSub.testRuntime().hasReceived(
            NewSub(
                TwitchChannel("otherchannel"),
                UserName("user")
            )
        )

        testEventHandler.shouldNotHaveBeenCalled()
    }

}) {
    companion object {
        private val CHANNEL = TwitchChannel("channel")
    }
}