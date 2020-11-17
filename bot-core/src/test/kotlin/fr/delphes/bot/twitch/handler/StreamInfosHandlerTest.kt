package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.StreamChanged
import fr.delphes.bot.event.incoming.StreamChanges
import fr.delphes.bot.event.incoming.StreamOffline
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.game.GameRepository
import fr.delphes.bot.webserver.payload.streamInfos.StreamInfosData
import fr.delphes.bot.webserver.payload.streamInfos.StreamInfosPayload
import fr.delphes.twitch.model.Game
import fr.delphes.twitch.model.SimpleGameId
import fr.delphes.twitch.model.Stream
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class StreamInfosHandlerTest {
    private val channelInfo = mockk<ChannelInfo>()
    private val changeState = mockk<ChannelChangeState>(relaxed = true)
    private val gameRepository = mockk<GameRepository>()

    private val STARTED_AT = LocalDateTime.of(2020, 1, 1, 12, 0)
    private val ONLINE_DATA = StreamInfosData(
        "id",
        "userId",
        "name",
        "game",
        null,
        "live",
        "current stream title",
        2,
        STARTED_AT,
        "fr",
        "url..."
    )

    private val GAME_ID = SimpleGameId("game")
    private val NEW_GAME_ID = SimpleGameId("new game")
    private val GAME = Game(GAME_ID, "game title")
    private val NEW_GAME = Game(NEW_GAME_ID, "new game title")

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        every { gameRepository.get(GAME_ID) } returns GAME
        every { gameRepository.get(NEW_GAME_ID) } returns NEW_GAME
    }

    @Nested
    @DisplayName("stream goes live")
    inner class StreamGoesLive {
        @Test
        internal fun `return event online`() {
            `given offline stream`()

            assertThat(
                StreamInfosHandler(gameRepository).handle(
                    StreamInfosPayload(ONLINE_DATA),
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
            `given offline stream`()

            StreamInfosHandler(gameRepository).handle(StreamInfosPayload(ONLINE_DATA), channelInfo, changeState)

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
    }

    @Nested
    @DisplayName("stream goes offline")
    inner class StreamGoesOffline {
        @Test
        internal fun `return offline event`() {
            `given online stream`()

            assertThat(
                StreamInfosHandler(gameRepository).handle(
                    StreamInfosPayload(),
                    channelInfo,
                    changeState
                )
            ).contains(
                StreamOffline
            )
        }

        @Test
        internal fun `change state`() {
            `given online stream`()

            StreamInfosHandler(gameRepository).handle(StreamInfosPayload(), channelInfo, changeState)

            verify(exactly = 1) { changeState.changeCurrentStream(null) }
        }
    }


    @Nested
    @DisplayName("update stream")
    inner class UpdateStream {
        @Test
        internal fun `return change title`() {
            `given online stream`()

            assertThat(
                StreamInfosHandler(gameRepository).handle(
                    StreamInfosPayload(ONLINE_DATA.copy(title = "new title")),
                    channelInfo,
                    changeState
                )
            ).contains(
                StreamChanged(StreamChanges.Title("current stream title", "new title"))
            )
        }

        @Test
        internal fun `return change game`() {
            `given online stream`()

            assertThat(
                StreamInfosHandler(gameRepository).handle(
                    StreamInfosPayload(ONLINE_DATA.copy(game_id = "new game")),
                    channelInfo,
                    changeState
                )
            ).contains(
                StreamChanged(StreamChanges.Game(GAME, NEW_GAME))
            )
        }

        @Test
        internal fun `don't update when no change`() {
            `given online stream`()

            val event = StreamInfosPayload(ONLINE_DATA.copy(title = "current stream title", game_id = "game"))

            assertThat(StreamInfosHandler(gameRepository).handle(event, channelInfo, changeState)).isEmpty()
        }
    }

    private fun `given offline stream`() {
        every { channelInfo.currentStream } returns null
    }

    private fun `given online stream`() {
        every { channelInfo.currentStream } returns Stream("current stream title", STARTED_AT, GAME)
    }
}