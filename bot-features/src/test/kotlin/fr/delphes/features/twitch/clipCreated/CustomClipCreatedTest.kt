package fr.delphes.features.twitch.clipCreated

import fr.delphes.connector.twitch.incomingEvent.ClipCreated
import fr.delphes.features.hasReceived
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.clips.Clip
import fr.delphes.twitch.api.streams.ThumbnailUrl
import fr.delphes.twitch.api.user.UserName
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class CustomClipCreatedTest : ShouldSpec({
    should("execute action if channel match") {
        var isCalled = false

        val customNewFollow = CustomClipCreated(CHANNEL) {
            isCalled = true
        }

        customNewFollow.hasReceived(
            ClipCreated(
                CHANNEL,
                Clip(
                    "url",
                    UserName("user"),
                    "gameId",
                    "title",
                    LocalDateTime.of(2021, 1, 1, 0, 0),
                    ThumbnailUrl("thumbnailUrl")
                )
            )
        )

        isCalled shouldBe true
    }
    should("not execute action if channel doesn't match") {
        var isCalled = false

        val customNewFollow = CustomClipCreated(CHANNEL) {
            isCalled = true
        }

        customNewFollow.hasReceived(
            ClipCreated(
                TwitchChannel("otherchannel"),
                Clip(
                    "url",
                    UserName("user"),
                    "gameId",
                    "title",
                    LocalDateTime.of(2021, 1, 1, 0, 0),
                    ThumbnailUrl("thumbnailUrl")
                )
            )
        )

        isCalled shouldBe false
    }

}) {
    companion object {
        private val CHANNEL = TwitchChannel("channel")
    }
}