package fr.delphes.features.twitch.streamOnline

import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.features.testRuntime
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class CustomStreamOnlineTest : ShouldSpec({
    should("execute action if channel match") {
        var isCalled = false

        val customStreamOnline = CustomStreamOnline(CHANNEL) {
            isCalled = true
        }

        customStreamOnline.testRuntime().hasReceived(
            streamOnlineFor(CHANNEL)
        )

        isCalled shouldBe true
    }
    should("not execute action if channel doesn't match") {
        var isCalled = false

        val customStreamOnline = CustomStreamOnline(CHANNEL) {
            isCalled = true
        }

        customStreamOnline.testRuntime().hasReceived(
            streamOnlineFor(TwitchChannel("otherchannel"))
        )

        isCalled shouldBe false
    }

}) {
    companion object {
        private val CHANNEL = TwitchChannel("channel")

        private fun streamOnlineFor(channel: TwitchChannel) = StreamOnline(
            channel,
            "id",
            "title",
            LocalDateTime.of(2021, 1, 1, 0, 0),
            Game(GameId("gameid"), "game"),
            "thumbnail url"
        )
    }
}
