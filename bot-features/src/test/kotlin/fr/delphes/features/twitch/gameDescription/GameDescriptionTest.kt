package fr.delphes.features.twitch.gameDescription

import fr.delphes.bot.Bot
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.games.WithGameId
import fr.delphes.twitch.api.user.UserName
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk

class GameDescriptionTest : ShouldSpec({
    val bot = mockk<Bot>()
    val connector = mockk<TwitchConnector>()

    fun `current game is`(game: Game) {
        every { bot.connectors } returns listOf(connector)
        every { connector.statistics.of(channel).currentStream?.game } returns game
    }

    afterTest {
        clearAllMocks()
    }

    should("describe the current") {
        val feature = GameDescription(channel, "!tufekoi", GAME_ID to "description")
        `current game is`(GAME)
        val commandAsked = CommandAsked(channel, Command("!tufekoi"), UserName("user"))

        val outgoingEvents = feature.handleIncomingEvent(commandAsked, bot)

        outgoingEvents.shouldContain(SendMessage("description", channel))
    }

    should("describe the current (with other gameId implementation)") {
        val feature = GameDescription(channel, "!tufekoi", GameEnum.GAME to "description")
        `current game is`(GAME)
        val commandAsked = CommandAsked(channel, Command("!tufekoi"), UserName("user"))

        val outgoingEvents = feature.handleIncomingEvent(commandAsked, bot)

        outgoingEvents.shouldContain(SendMessage("description", channel))
    }

    should("do nothing when no description") {
        val feature = GameDescription(channel, "!tufekoi", GAME_ID to "description")
        `current game is`(OTHER_GAME)
        val commandAsked = CommandAsked(channel, Command("!tufekoi"), UserName("user"))

        val outgoingEvents = feature.handleIncomingEvent(commandAsked, bot)

        outgoingEvents.shouldBeEmpty()
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
    }
}
