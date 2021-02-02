package fr.delphes.bot.twitch.handler

import fr.delphes.bot.Channel
import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.state.ChannelState
import fr.delphes.bot.util.time.TestClock
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.streamOnline.StreamType
import fr.delphes.twitch.api.streams.Stream
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import fr.delphes.twitch.api.streamOnline.StreamOnline as StreamOnlineTwitch

internal class StreamOnlineHandlerTest {
    private val state = mockk<ChannelState>(relaxed = true)
    private val bot = mockk<ClientBot>()
    private val channel = mockk<Channel>()

    private val GAME_ID = GameId("game")
    private val GAME = Game(GAME_ID, "label")
    private val STARTED_AT = LocalDateTime.of(2020, 1, 1, 12, 0)
    private val THUMBNAIL_URL = "thumbnail_url"
    private val CHANNEL = TwitchChannel("channel")

    private val streamOnlineHandler = StreamOnlineHandler(channel, bot, TestClock(STARTED_AT))

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        `given offline stream`()

        every { bot.channelOf(CHANNEL) } returns channel
        every { channel.state } returns state

        coEvery { channel.twitchApi.getStream() } returns Stream(
            "streamId",
            "current stream title",
            STARTED_AT,
            GAME,
            THUMBNAIL_URL
        )
    }

    @Test
    internal fun `return event online`() {
        assertThat(
            runBlocking {
                streamOnlineHandler.handle(
                    StreamOnlineTwitch(CHANNEL, StreamType.LIVE)
                )
            }
        )
            .contains(
                StreamOnline(
                    CHANNEL,
                    "current stream title",
                    STARTED_AT,
                    GAME,
                    THUMBNAIL_URL
                )
            )
    }

    @Test
    internal fun `change state`() {
        runBlocking {
            streamOnlineHandler.handle(
                StreamOnlineTwitch(CHANNEL, StreamType.LIVE)
            )
        }

        verify(exactly = 1) {
            state.changeCurrentStream(
                Stream(
                    "streamId",
                    "current stream title",
                    STARTED_AT,
                    GAME,
                    THUMBNAIL_URL
                )
            )
        }
    }

    @Test
    internal fun `don't notify if stream already online`() {
        `given online stream`()

        runBlocking {

            assertThat(
                streamOnlineHandler.handle(
                    StreamOnlineTwitch(CHANNEL, StreamType.LIVE)
                )
            ).isEmpty()
        }

        verify(exactly = 0) {
            state.changeCurrentStream(any())
        }
    }

    private fun `given offline stream`() {
        every { channel.isOnline() } returns false
    }

    private fun `given online stream`() {
        every { channel.isOnline() } returns true
    }
}