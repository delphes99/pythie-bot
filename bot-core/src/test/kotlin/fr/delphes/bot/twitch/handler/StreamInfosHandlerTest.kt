package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.StreamChanged
import fr.delphes.bot.event.incoming.StreamChanges
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.twitch.api.channelUpdate.ChannelUpdate
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.SimpleGameId
import fr.delphes.twitch.model.Stream
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class StreamInfosHandlerTest {
    private val channelInfo = mockk<ChannelInfo>()
    private val changeState = mockk<ChannelChangeState>(relaxed = true)

    private val STARTED_AT = LocalDateTime.of(2020, 1, 1, 12, 0)

    private val CURRENT_TITLE = "current stream title"
    private val CURRENT_GAME_ID = SimpleGameId("game")
    private val CURRENT_GAME = Game(CURRENT_GAME_ID, "game title")

    private val NEW_TITLE = "new title"
    private val NEW_GAME_ID = SimpleGameId("new game")
    private val NEW_GAME = Game(NEW_GAME_ID, "new game title")

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        `given online stream`()
    }

    private val channelUpdateHandler = ChannelUpdateHandler()

    @Test
    internal fun `return change title`() {
        assertThat(
            channelUpdateHandler.handle(
                ChannelUpdate(NEW_TITLE, "en", CURRENT_GAME),
                channelInfo,
                changeState
            )
        ).contains(
            StreamChanged(StreamChanges.Title(CURRENT_TITLE, "new title"))
        )
    }

    @Test
    internal fun `return change game`() {
        assertThat(
            channelUpdateHandler.handle(
                ChannelUpdate(CURRENT_TITLE, "en", NEW_GAME),
                channelInfo,
                changeState
            )
        ).contains(
            StreamChanged(StreamChanges.Game(CURRENT_GAME, NEW_GAME))
        )
    }

    @Test
    internal fun `don't update when no change`() {
        val event = ChannelUpdate(CURRENT_TITLE, "en", CURRENT_GAME)

        assertThat(channelUpdateHandler.handle(event, channelInfo, changeState)).isEmpty()
    }

    private fun `given online stream`() {
        every { channelInfo.currentStream } returns Stream(CURRENT_TITLE, STARTED_AT, CURRENT_GAME)
    }
}