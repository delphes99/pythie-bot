package fr.delphes.feature.gamedescription

import fr.delphes.twitch.model.User
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.command.Command
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.feature.gameDescription.GameDescription
import fr.delphes.feature.handle
import fr.delphes.twitch.model.Game
import fr.delphes.twitch.model.GameId
import fr.delphes.twitch.model.SimpleGameId
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GameDescriptionTest {
    private val channelInfo = mockk<ChannelInfo>()

    private val GAME_ID = SimpleGameId("id")
    private val GAME = Game(GAME_ID, "label")
    private val OTHER_GAME_ID = SimpleGameId("otherId")
    private val OTHER_GAME = Game(OTHER_GAME_ID, "other label")
    enum class GameEnum(override val id: String) : GameId {
        GAME("id");
    }

    @Test
    internal suspend fun `describe the current`() {
        val feature = GameDescription("!tufekoi", GAME_ID to "description")
        `current game is`(GAME)
        val commandAsked = CommandAsked(Command("!tufekoi"), User("user"))

        val outgoingEvents = feature.handle(commandAsked, channelInfo)

        assertThat(outgoingEvents).contains(SendMessage("description"))
    }

    @Test
    internal suspend fun `describe the current (with other gameId implementation)`() {
        val feature = GameDescription("!tufekoi", GameEnum.GAME to "description")
        `current game is`(GAME)
        val commandAsked = CommandAsked(Command("!tufekoi"), User("user"))

        val outgoingEvents = feature.handle(commandAsked, channelInfo)

        assertThat(outgoingEvents).contains(SendMessage("description"))
    }

    @Test
    internal suspend fun `do nothing when no description`() {
        val feature = GameDescription("!tufekoi", GAME_ID to "description")
        `current game is`(OTHER_GAME)
        val commandAsked = CommandAsked(Command("!tufekoi"), User("user"))

        val outgoingEvents = feature.handle(commandAsked, channelInfo)

        assertThat(outgoingEvents).isEmpty()
    }

    private fun `current game is`(game: Game) {
        every { channelInfo.currentStream?.game } returns game
    }
}
