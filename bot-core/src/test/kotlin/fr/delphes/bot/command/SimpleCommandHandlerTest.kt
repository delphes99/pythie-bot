package fr.delphes.bot.command

import fr.delphes.twitch.api.user.User
import fr.delphes.bot.Channel
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.bot.util.time.Clock
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

internal class SimpleCommandHandlerTest {
    private val clock = mockk<Clock>()
    private val now = LocalDateTime.of(2020, 1, 1, 0, 0)
    private val channel = mockk<Channel>()

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        `given now`(now)
    }

    private fun `given now`(now: LocalDateTime) {
        every { clock.now() } returns now
    }

    @Test
    internal suspend fun `trigger command`() {
        val command = Command("!cmd")
        val simpleCommand = SimpleCommandHandler(
            command,
            clock = clock,
            responses = listOf(SendMessage("response"))
        )
        val event = CommandAsked(command, User("user"))

        assertThat(simpleCommand.handle(event, channel)).containsExactlyInAnyOrder(
            SendMessage("response")
        )
    }

    @Test
    internal suspend fun `don't trigger command if too frequent`() {
        val command = Command("!cmd")
        val simpleCommand = SimpleCommandHandler(
            command = command,
            clock = clock,
            cooldown = Duration.ofMinutes(10),
            responses = listOf(SendMessage("response"))
        )
        val event = CommandAsked(command, User("user"))

        `given now`(now)
        assertThat(simpleCommand.handle(event, channel)).isNotEmpty

        `given now`(now.plusMinutes(5))
        assertThat(simpleCommand.handle(event, channel)).isEmpty()
    }

    @Test
    internal suspend fun `trigger command if after cooldown`() {
        val command = Command("!cmd")
        val simpleCommand = SimpleCommandHandler(
            command = command,
            clock = clock,
            cooldown = Duration.ofMinutes(10),
            responses = listOf(SendMessage("response"))
        )
        val event = CommandAsked(command, User("user"))

        `given now`(now)
        assertThat(simpleCommand.handle(event, channel)).isNotEmpty

        `given now`(now.plusMinutes(15))
        assertThat(simpleCommand.handle(event, channel)).isNotEmpty
    }
}