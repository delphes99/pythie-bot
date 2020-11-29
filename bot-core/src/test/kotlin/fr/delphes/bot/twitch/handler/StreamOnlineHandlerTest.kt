package fr.delphes.bot.twitch.handler

import fr.delphes.bot.Channel
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.game.GameRepository
import fr.delphes.bot.util.time.TestClock
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.SimpleGameId
import fr.delphes.twitch.api.streamOnline.StreamType
import fr.delphes.twitch.model.Stream
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import fr.delphes.twitch.api.streamOnline.StreamOnline as StreamOnlineTwitch

internal class StreamOnlineHandlerTest {
    private val channelInfo = mockk<ChannelInfo>()
    private val changeState = mockk<ChannelChangeState>(relaxed = true)
    private val channel = mockk<Channel>()

    private val GAME_ID = SimpleGameId("game")
    private val GAME = Game(GAME_ID, "label")
    private val STARTED_AT = LocalDateTime.of(2020, 1, 1, 12, 0)

    private val streamOnlineHandler = StreamOnlineHandler(channel, TestClock(STARTED_AT))
    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        `given offline stream`()
        coEvery { channel.twitchApi.getStream() } returns Stream("current stream title", STARTED_AT, GAME)
    }

    @Test
    internal fun `return event online`() {
        assertThat(
            streamOnlineHandler.handle(
                StreamOnlineTwitch(StreamType.LIVE),
                channelInfo,
                changeState
            )
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
        streamOnlineHandler.handle(
            StreamOnlineTwitch(StreamType.LIVE),
            channelInfo,
            changeState
        )

        verify(exactly = 1) {
            changeState.changeCurrentStream(
                Stream(
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