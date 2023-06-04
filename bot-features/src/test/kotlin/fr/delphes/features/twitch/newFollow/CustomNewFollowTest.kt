package fr.delphes.features.twitch.newFollow

import fr.delphes.connector.twitch.incomingEvent.NewFollow
import fr.delphes.features.TestIncomingEventHandlerAction
import fr.delphes.features.testRuntime
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import io.kotest.core.spec.style.ShouldSpec

class CustomNewFollowTest : ShouldSpec({
    should("execute action if channel match") {
        val testEventHandler = TestIncomingEventHandlerAction<NewFollow>()

        val customNewFollow = CustomNewFollow(CHANNEL, action = testEventHandler)

        customNewFollow.testRuntime().hasReceived(
            NewFollow(
                CHANNEL,
                UserName("user")
            )
        )

        testEventHandler.shouldHaveBeenCalled()
    }
    should("not execute action if channel doesn't match") {
        val testEventHandler = TestIncomingEventHandlerAction<NewFollow>()

        val customNewFollow = CustomNewFollow(CHANNEL, action = testEventHandler)

        customNewFollow.testRuntime().hasReceived(
            NewFollow(
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
