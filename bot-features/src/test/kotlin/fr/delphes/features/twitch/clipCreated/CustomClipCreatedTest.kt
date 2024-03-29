package fr.delphes.features.twitch.clipCreated

import fr.delphes.connector.twitch.incomingEvent.ClipCreated
import fr.delphes.features.TestIncomingEventHandlerAction
import fr.delphes.features.testRuntime
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.clips.Clip
import fr.delphes.twitch.api.streams.ThumbnailUrl
import fr.delphes.twitch.api.user.UserName
import io.kotest.core.spec.style.ShouldSpec
import java.time.LocalDateTime

class CustomClipCreatedTest : ShouldSpec({
    should("execute action if channel match") {
        val testEventHandler = TestIncomingEventHandlerAction<ClipCreated>()

        val customNewFollow = CustomClipCreated(CHANNEL, action = testEventHandler)

        customNewFollow.testRuntime().hasReceived(
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

        testEventHandler.shouldHaveBeenCalled()
    }
    should("not execute action if channel doesn't match") {
        val testEventHandler = TestIncomingEventHandlerAction<ClipCreated>()

        val customNewFollow = CustomClipCreated(CHANNEL, action = testEventHandler)

        customNewFollow.testRuntime().hasReceived(
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

        testEventHandler.shouldNotHaveBeenCalled()
    }

}) {
    companion object {
        private val CHANNEL = TwitchChannel("channel")
    }
}