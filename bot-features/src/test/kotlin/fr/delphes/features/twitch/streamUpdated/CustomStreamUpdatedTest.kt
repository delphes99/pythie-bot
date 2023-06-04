package fr.delphes.features.twitch.streamUpdated

import fr.delphes.connector.twitch.incomingEvent.StreamChanged
import fr.delphes.features.TestIncomingEventHandlerAction
import fr.delphes.features.testRuntime
import fr.delphes.twitch.TwitchChannel
import io.kotest.core.spec.style.ShouldSpec

class CustomStreamUpdatedTest : ShouldSpec({
    should("execute action if channel match") {
        val testEventHandler = TestIncomingEventHandlerAction<StreamChanged>()

        val customStreamUpdated = CustomStreamUpdated(CHANNEL, action = testEventHandler)

        customStreamUpdated.testRuntime().hasReceived(
            StreamChanged(
                CHANNEL
            )
        )

        testEventHandler.shouldHaveBeenCalled()
    }
    should("not execute action if channel doesn't match") {
        val testEventHandler = TestIncomingEventHandlerAction<StreamChanged>()

        val customStreamUpdated = CustomStreamUpdated(CHANNEL, action = testEventHandler)

        customStreamUpdated.testRuntime().hasReceived(
            StreamChanged(
                TwitchChannel("otherchannel")
            )
        )

        testEventHandler.shouldNotHaveBeenCalled()
    }
}) {
    companion object {
        private val CHANNEL = TwitchChannel("channel")
    }
}
