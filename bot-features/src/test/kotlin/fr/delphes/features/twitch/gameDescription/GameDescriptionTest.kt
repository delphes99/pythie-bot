package fr.delphes.features.twitch.gameDescription

import fr.delphes.bot.ClientBot
import fr.delphes.bot.command.Command
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.features.handle
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.games.WithGameId
import fr.delphes.twitch.api.user.User
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GameDescriptionTest {
    private val clientBot = mockk<ClientBot>()

    private val CHANNEL = TwitchChannel("channel")
    private val GAME_ID = GameId("id")
    private val GAME = Game(GAME_ID, "label")
    private val OTHER_GAME_ID = GameId("otherId")
    private val OTHER_GAME = Game(OTHER_GAME_ID, "other label")
    enum class GameEnum(override val id: String) : WithGameId {
        GAME("id");
    }

    @Test
    internal suspend fun `describe the current`() {
        val feature = GameDescription(CHANNEL, "!tufekoi", GAME_ID to "description")
        `current game is`(GAME)
        val commandAsked = CommandAsked(CHANNEL, Command("!tufekoi"), User("user"))

        val outgoingEvents = feature.handle(commandAsked, clientBot)

        assertThat(outgoingEvents).contains(SendMessage("description"))
    }

    @Test
    internal suspend fun `describe the current (with other gameId implementation)`() {
        val feature = GameDescription(CHANNEL,"!tufekoi", GameEnum.GAME to "description")
        `current game is`(GAME)
        val commandAsked = CommandAsked(CHANNEL, Command("!tufekoi"), User("user"))

        val outgoingEvents = feature.handle(commandAsked, clientBot)

        assertThat(outgoingEvents).contains(SendMessage("description"))
    }

    @Test
    internal suspend fun `do nothing when no description`() {
        val feature = GameDescription(CHANNEL, "!tufekoi", GAME_ID to "description")
        `current game is`(OTHER_GAME)
        val commandAsked = CommandAsked(CHANNEL, Command("!tufekoi"), User("user"))

        val outgoingEvents = feature.handle(commandAsked, clientBot)

        assertThat(outgoingEvents).isEmpty()
    }

    private fun `current game is`(game: Game) {
        every { clientBot.channelOf(CHANNEL)?.currentStream?.game } returns game
    }
}
