package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.StreamOffline
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.SimpleGameId
import fr.delphes.twitch.model.Stream
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import fr.delphes.twitch.api.streamOffline.StreamOffline as StreamOfflineTwitch

class StreamOfflineHandlerTest {
    private val channelInfo = mockk<ChannelInfo>()
    private val changeState = mockk<ChannelChangeState>(relaxed = true)

    private val GAME_ID = SimpleGameId("game")
    private val GAME = Game(GAME_ID, "label")
    private val STARTED_AT = LocalDateTime.of(2020, 1, 1, 12, 0)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        `given online stream`()
    }

    @Test
    internal fun `return offline event`() {assertThat(
            StreamOfflineHandler().handle(
                StreamOfflineTwitch,
                channelInfo,
                changeState
            )
        ).contains(
            StreamOffline
        )
    }

    @Test
    internal fun `change state`() {
        StreamOfflineHandler().handle(StreamOfflineTwitch, channelInfo, changeState)

        verify(exactly = 1) { changeState.changeCurrentStream(null) }
    }

    private fun `given online stream`() {
        every { channelInfo.currentStream } returns Stream("current stream title", STARTED_AT, GAME)
    }
}