package fr.delphes.bot.twitch.handler

import fr.delphes.bot.Channel
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.util.time.TestClock
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
    private val channelInfo = mockk<ChannelInfo>()
    private val changeState = mockk<ChannelChangeState>(relaxed = true)
    private val channel = mockk<Channel>()

    private val GAME_ID = GameId("game")
    private val GAME = Game(GAME_ID, "label")
    private val STARTED_AT = LocalDateTime.of(2020, 1, 1, 12, 0)

    private val streamOnlineHandler = StreamOnlineHandler(channel, TestClock(STARTED_AT))
    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        `given offline stream`()
        coEvery { channel.twitchApi.getStream() } returns Stream("streamId", "current stream title", STARTED_AT, GAME)
    }

    @Test
    internal fun `return event online`() {
        assertThat(
            runBlocking {
                streamOnlineHandler.handle(
                    StreamOnlineTwitch(StreamType.LIVE),
                    channelInfo,
                    changeState
                )
            }
        )
            .contains(
                StreamOnline(
                    "current stream title",
                    STARTED_AT,
                    GAME
                )
            )
    }

    @Test
    internal fun `change state`() {
        runBlocking {
            streamOnlineHandler.handle(
                StreamOnlineTwitch(StreamType.LIVE),
                channelInfo,
                changeState
            )
        }

        verify(exactly = 1) {
            changeState.changeCurrentStream(
                Stream(
                    "streamId",
                    "current stream title",
                    STARTED_AT,
                    GAME
                )
            )
        }
    }

    private fun `given offline stream`() {
        every { channelInfo.currentStream } returns null
    }
}