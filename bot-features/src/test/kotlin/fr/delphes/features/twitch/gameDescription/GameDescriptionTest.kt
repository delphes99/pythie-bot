package fr.delphes.features.twitch.gameDescription

import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.connector.twitch.state.GetCurrentStreamState
import fr.delphes.features.CustomFeatureRuntimeBuilder
import fr.delphes.features.testRuntime
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.games.WithGameId
import fr.delphes.twitch.api.user.UserName
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every

class GameDescriptionTest : ShouldSpec({
    should("describe the current") {
        val feature = GameDescriptionFeature(channel, "!tufekoi", GAME_ID to "description")

        val executionContext = feature.testRuntime {
            `current game is`(GAME)
        }.hasReceived(CommandAsked(channel, Command("!tufekoi"), UserName("user")))

        executionContext.shouldHaveExecuteOutgoingEvent(SendMessage(channel, "description"))
    }
    should("describe the current (with other gameId implementation)") {
        val feature = GameDescriptionFeature(channel, "!tufekoi", GameEnum.GAME to "description")

        val executionContext = feature.testRuntime {
            `current game is`(GAME)
        }.hasReceived(CommandAsked(channel, Command("!tufekoi"), UserName("user")))

        executionContext.shouldHaveExecuteOutgoingEvent(SendMessage(channel, "description"))
    }
    should("do nothing when no description") {
        val feature = GameDescriptionFeature(channel, "!tufekoi", GAME_ID to "description")

        val executionContext = feature.testRuntime {
            `current game is`(OTHER_GAME)
        }.hasReceived(CommandAsked(channel, Command("!tufekoi"), UserName("user")))

        executionContext.shouldHaveNotExecuteOutgoingEvent(SendMessage(channel, "description"))
    }
}) {
    companion object {
        private val channel = TwitchChannel("channel")
        private val GAME_ID = GameId("id")
        private val GAME = Game(GAME_ID, "label")
        private val OTHER_GAME_ID = GameId("otherId")
        private val OTHER_GAME = Game(OTHER_GAME_ID, "other label")

        enum class GameEnum(override val id: String) : WithGameId {
            GAME("id");
        }

        private fun CustomFeatureRuntimeBuilder.`current game is`(game: Game) {
            withMockedState(GetCurrentStreamState.ID) {
                every { getStreamInfosOf(channel)?.game } returns game
            }
        }
    }
}