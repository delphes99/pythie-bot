package fr.delphes.features.twitch.streamOffline

import fr.delphes.features.TestIncomingEventHandlerAction
import fr.delphes.features.testRuntime
import fr.delphes.twitch.TwitchChannel
import io.kotest.core.spec.style.ShouldSpec
import fr.delphes.connector.twitch.incomingEvent.StreamOffline as StreamOfflineEvent

class CustomStreamOfflineTest : ShouldSpec({
    should("execute action if channel match") {
        val testEventHandler = TestIncomingEventHandlerAction<StreamOfflineEvent>()

        val customStreamOffline = CustomStreamOffline(CHANNEL, action = testEventHandler)

        customStreamOffline.testRuntime().hasReceived(
            StreamOfflineEvent(
                CHANNEL
            )
        )

        testEventHandler.shouldHaveBeenCalled()
    }
    should("not execute action if channel doesn't match") {
        val testEventHandler = TestIncomingEventHandlerAction<StreamOfflineEvent>()

        val customStreamOffline = CustomStreamOffline(CHANNEL, action = testEventHandler)

        customStreamOffline.testRuntime().hasReceived(
            StreamOfflineEvent(
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
