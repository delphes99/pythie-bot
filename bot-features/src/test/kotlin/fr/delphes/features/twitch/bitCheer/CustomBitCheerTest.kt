package fr.delphes.features.twitch.bitCheer

import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.features.TestIncomingEventHandlerAction
import fr.delphes.features.testRuntime
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import io.kotest.core.spec.style.ShouldSpec

class CustomBitCheerTest : ShouldSpec({
    should("execute action if channel match") {
        val testEventHandler = TestIncomingEventHandlerAction<BitCheered>()

        val customNewFollow = CustomBitCheer(CHANNEL, action = testEventHandler)

        customNewFollow.testRuntime().hasReceived(
            BitCheered(
                CHANNEL,
                UserName("user"),
                100L,
                "message"
            )
        )

        testEventHandler.shouldHaveBeenCalled()
    }
    should("not execute action if channel doesn't match") {
        val testEventHandler = TestIncomingEventHandlerAction<BitCheered>()

        val customNewFollow = CustomBitCheer(CHANNEL, action = testEventHandler)

        customNewFollow.testRuntime().hasReceived(
            BitCheered(
                TwitchChannel("otherchannel"),
                UserName("user"),
                100L,
                "message"
            )
        )

        testEventHandler.shouldNotHaveBeenCalled()
    }

}) {
    companion object {
        private val CHANNEL = TwitchChannel("channel")
    }
}