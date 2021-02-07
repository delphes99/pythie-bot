package fr.delphes.connector.twitch.eventMapper

import fr.delphes.bot.state.ChannelState
import fr.delphes.connector.twitch.ClientBot
import fr.delphes.connector.twitch.incomingEvent.StreamChanged
import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelUpdate.ChannelUpdate
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.streams.Stream
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class StreamInfosHandlerTest {
    private val channelState = mockk<ChannelState>(relaxed = true)
    private val bot = mockk<ClientBot>()

    private val STARTED_AT = LocalDateTime.of(2020, 1, 1, 12, 0)

    private val CURRENT_TITLE = "current stream title"
    private val CURRENT_GAME_ID = GameId("game")
    private val CURRENT_GAME = Game(CURRENT_GAME_ID, "game title")

    private val NEW_TITLE = "new title"
    private val NEW_GAME_ID = GameId("new game")
    private val NEW_GAME = Game(NEW_GAME_ID, "new game title")

    private val CHANNEL = TwitchChannel("channel")

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        `given online stream`()

        every {
            bot.channelOf(CHANNEL)?.state
        } returns channelState

    }

    private val channelUpdateHandler = ChannelUpdateMapper(bot)

    @Test
    internal fun `return change title`() {
        assertThat(
            runBlocking {
                channelUpdateHandler.handle(
                    ChannelUpdate(CHANNEL, NEW_TITLE, "en", CURRENT_GAME)
                )
            }
        ).contains(
            StreamChanged(CHANNEL, StreamChanges.Title(CURRENT_TITLE, "new title"))
        )
    }

    @Test
    internal fun `return change game`() {
        assertThat(
            runBlocking {
                channelUpdateHandler.handle(
                    ChannelUpdate(CHANNEL, CURRENT_TITLE, "en", NEW_GAME)
                )
            }
        ).contains(
            StreamChanged(CHANNEL, StreamChanges.Game(CURRENT_GAME, NEW_GAME))
        )
    }

    @Test
    internal fun `don't update when no change`() {
        val event = ChannelUpdate(CHANNEL, CURRENT_TITLE, "en", CURRENT_GAME)

        assertThat(
            runBlocking {
                channelUpdateHandler.handle(event)
            }
        ).isEmpty()
    }

    private fun `given online stream`() {
        every { bot.channelOf(CHANNEL)?.currentStream } returns Stream(
            "someId",
            CURRENT_TITLE,
            STARTED_AT,
            CURRENT_GAME,
            ""
        )
    }
}