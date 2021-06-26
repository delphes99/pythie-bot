package fr.delphes.connector.twitch.state.reducer

import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.connector.twitch.state.GameInfo
import fr.delphes.connector.twitch.state.StreamInfos
import fr.delphes.connector.twitch.state.TwitchChannelState
import fr.delphes.connector.twitch.state.TwitchConnectorState
import fr.delphes.connector.twitch.state.action.StreamChangeAction
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class StreamChangeReducerTest {
    private val channel = TwitchChannel("channel")

    @Test
    internal fun initialState() {
        TwitchConnectorState().stateOf(channel).currentStream.shouldBeNull()
    }

    @Test
    internal fun `change title`() {
        val initialState = TwitchConnectorState(
            channel to TwitchChannelState(
                currentStream = StreamInfos(
                    "id",
                    "current title",
                    LocalDateTime.of(2021, 6, 27, 12, 0),
                    GameInfo(GameId("currentGameId"), "current game")
                )
            )
        )
        val action = StreamChangeAction(
            channel,
            listOf(
                StreamChanges.Title("current title", "new title"),
            )
        )

        val newState = StreamChangeReducer().apply(action, initialState)

        newState.stateOf(channel).currentStream?.title shouldBe "new title"
    }

    @Test
    internal fun `change game`() {
        val initialState = TwitchConnectorState(
            channel to TwitchChannelState(
                currentStream = StreamInfos(
                    "id",
                    "current title",
                    LocalDateTime.of(2021, 6, 27, 12, 0),
                    GameInfo(GameId("currentGameId"), "current game")
                )
            )
        )
        val action = StreamChangeAction(
            channel,
            listOf(
                StreamChanges.Game(
                    Game(GameId("currentGameId"), "current game"),
                    Game(GameId("newGameId"), "new game")
                )
            )
        )

        val newState = StreamChangeReducer().apply(action, initialState)

        newState.stateOf(channel).currentStream?.game shouldBe GameInfo(GameId("newGameId"), "new game")
    }
}