package fr.delphes.features.twitch.gameDescription

import fr.delphes.bot.Bot
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.TwitchState
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.features.handle
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.games.WithGameId
import fr.delphes.twitch.api.user.User
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GameDescriptionTest {
    private val bot = mockk<Bot>()
    private val connector = mockk<TwitchConnector>()

    private val channel = TwitchChannel("channel")
    private val GAME_ID = GameId("id")
    private val GAME = Game(GAME_ID, "label")
    private val OTHER_GAME_ID = GameId("otherId")
    private val OTHER_GAME = Game(OTHER_GAME_ID, "other label")
    enum class GameEnum(override val id: String) : WithGameId {
        GAME("id");
    }

    @Test
    internal suspend fun `describe the current`() {
        val feature = GameDescription(channel, "!tufekoi", GAME_ID to "description")
        `current game is`(GAME)
        val commandAsked = CommandAsked(channel, Command("!tufekoi"), User("user"))

        val outgoingEvents = feature.handle(commandAsked, bot)

        assertThat(outgoingEvents).contains(SendMessage("description", channel))
    }

    @Test
    internal suspend fun `describe the current (with other gameId implementation)`() {
        val feature = GameDescription(channel, "!tufekoi", GameEnum.GAME to "description")
        `current game is`(GAME)
        val commandAsked = CommandAsked(channel, Command("!tufekoi"), User("user"))

        val outgoingEvents = feature.handle(commandAsked, bot)

        assertThat(outgoingEvents).contains(SendMessage("description", channel))
    }

    @Test
    internal suspend fun `do nothing when no description`() {
        val feature = GameDescription(channel, "!tufekoi", GAME_ID to "description")
        `current game is`(OTHER_GAME)
        val commandAsked = CommandAsked(channel, Command("!tufekoi"), User("user"))

        val outgoingEvents = feature.handle(commandAsked, bot)

        assertThat(outgoingEvents).isEmpty()
    }

    private fun `current game is`(game: Game) {
        val slot = slot<suspend TwitchState.AppConnected.() -> List<OutgoingEvent>>()

        val appConnected = mockk<TwitchState.AppConnected>()
        coEvery {
            connector.whenRunning(
                whenRunning = capture(slot),
                whenNotRunning = any()
            )
        } answers {
            runBlocking { slot.captured(appConnected) }
        }

        every { appConnected.clientBot.channelOf(channel)?.currentStream?.game } returns game
    }
}
