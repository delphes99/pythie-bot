package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.incoming.StreamOffline
import fr.delphes.bot.state.ChannelState
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import fr.delphes.twitch.api.streamOffline.StreamOffline as StreamOfflineTwitch

class StreamOfflineHandlerTest {
    private val changeState = mockk<ChannelState>(relaxed = true)
    private val bot = mockk<ClientBot>()

    private val GAME_ID = GameId("game")
    private val GAME = Game(GAME_ID, "label")
    private val STARTED_AT = LocalDateTime.of(2020, 1, 1, 12, 0)
    private val CHANNEL = TwitchChannel("channel")

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        every { bot.channelOf(CHANNEL)?.state } returns changeState
    }

    @Test
    internal fun `return offline event`() {
        assertThat(
            runBlocking {
                StreamOfflineHandler(bot).handle(
                    StreamOfflineTwitch(CHANNEL)
                )
            }
        ).contains(
            StreamOffline(CHANNEL)
        )
    }

    @Test
    internal fun `change state`() {
        runBlocking {
            StreamOfflineHandler(bot).handle(StreamOfflineTwitch(CHANNEL))
        }

        verify(exactly = 1) { changeState.changeCurrentStream(null) }
    }

}