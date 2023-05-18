package fr.delphes.features.twitch.streamerHighlight

import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.connector.twitch.state.GetUserInfos
import fr.delphes.connector.twitch.user.UserInfos
import fr.delphes.features.testRuntime
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.api.user.UserName
import fr.delphes.twitch.api.user.payload.BroadcasterType
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.coEvery
import io.mockk.mockk
import java.time.Duration
import java.time.LocalDateTime

class StreamerHighlightFeatureTest : ShouldSpec({
    should("should shoutout a active streamer who speak in chat") {
        var isCalled = false

        val command = StreamerHighlightFeature(
            channel = CHANNEL,
            highlightExpiration = Duration.ofMinutes(5),
            activeStreamer = Duration.ofMinutes(60),
        ) { message, userInfos ->
            isCalled = true
            mockk()
        }

        command.testRuntime {
            withState(GetUserInfos(mockk {
                coEvery { getUser(any()) } returns UserInfos(
                    "user",
                    UserId("userId"),
                    BroadcasterType.AFFILIATE,
                    100,
                    "last stream title",
                    NOW.minusMinutes(10)
                )
            }))
            atFixedTime(NOW)
        }.hasReceived(
            MessageReceived(
                CHANNEL,
                USER,
                "message"
            )
        )

        isCalled.shouldBeTrue()
    }
    should("should not shoutout a streamer which has not streamed too recently") {
        var isCalled = false

        val command = StreamerHighlightFeature(
            channel = CHANNEL,
            highlightExpiration = Duration.ofMinutes(5),
            activeStreamer = Duration.ofMinutes(60),
        ) { message, userInfos ->
            isCalled = true
            mockk()
        }

        command.testRuntime {
            withState(GetUserInfos(mockk {
                coEvery { getUser(any()) } returns UserInfos(
                    "user",
                    UserId("userId"),
                    BroadcasterType.AFFILIATE,
                    100,
                    "last stream title",
                    NOW.minusMonths(10)
                )
            }))
            atFixedTime(NOW)
        }.hasReceived(
            MessageReceived(
                CHANNEL,
                USER,
                "message"
            )
        )

        isCalled.shouldBeFalse()
    }
    should("should not shoutout a viewer which has not streamed") {
        var isCalled = false

        val command = StreamerHighlightFeature(
            channel = CHANNEL,
            highlightExpiration = Duration.ofMinutes(5),
            activeStreamer = Duration.ofMinutes(60),
        ) { message, userInfos ->
            isCalled = true
            mockk()
        }

        command.testRuntime {
            withState(GetUserInfos(mockk {
                coEvery { getUser(any()) } returns UserInfos(
                    "user",
                    UserId("userId"),
                    BroadcasterType.DEFAULT,
                    0
                )
            }))
            atFixedTime(NOW)
        }.hasReceived(
            MessageReceived(
                CHANNEL,
                USER,
                "message"
            )
        )

        isCalled.shouldBeFalse()
    }
    should("should not shoutout a active streamer too frequently") {
        var isCalled = false

        val command = StreamerHighlightFeature(
            channel = CHANNEL,
            highlightExpiration = Duration.ofMinutes(5),
            activeStreamer = Duration.ofMinutes(60),
        ) { message, userInfos ->
            isCalled = true
            mockk()
        }

        command.testRuntime {
            withState(GetUserInfos(mockk {
                coEvery { getUser(any()) } returns UserInfos(
                    "user",
                    UserId("userId"),
                    BroadcasterType.AFFILIATE,
                    100,
                    "last stream title",
                    NOW.minusMinutes(10)
                )
            }))
            atFixedTime(NOW)
            withState(
                StreamerHighlightState(
                    CHANNEL,
                    mutableMapOf(USER to NOW.minusMinutes(30))
                )
            )
        }.hasReceived(
            MessageReceived(
                CHANNEL,
                USER,
                "message"
            )
        )

        isCalled.shouldBeTrue()
    }
}) {
    companion object {
        private val NOW = LocalDateTime.of(2021, 1, 1, 0, 0, 0)
        private val CHANNEL = TwitchChannel("channel")
        private val USER = UserName("user")
    }
}