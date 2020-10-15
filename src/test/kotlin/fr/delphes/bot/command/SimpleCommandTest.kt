package fr.delphes.bot.command

import fr.delphes.User
import fr.delphes.bot.Channel
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.bot.time.Clock
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

internal class SimpleCommandTest {
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
    internal fun `trigger command`() {
        val simpleCommand = SimpleCommand(
            "!cmd",
            clock = clock,
            responses = listOf(SendMessage("response"))
        )
        val event = CommandAsked(simpleCommand, User("user"))

        assertThat(simpleCommand.handle(event, channel)).containsExactlyInAnyOrder(
            SendMessage("response")
        )
    }

    @Test
    internal fun `don't trigger command if too frequent`() {
        val simpleCommand = SimpleCommand(
            triggerMessage = "!cmd",
            clock = clock,
            cooldown = Duration.ofMinutes(10),
            responses = listOf(SendMessage("response"))
        )
        val event = CommandAsked(simpleCommand, User("user"))

        `given now`(now)
        assertThat(simpleCommand.handle(event, channel)).isNotEmpty

        `given now`(now.plusMinutes(5))
        assertThat(simpleCommand.handle(event, channel)).isEmpty()
    }

    @Test
    internal fun `trigger command if after cooldown`() {
        val simpleCommand = SimpleCommand(
            triggerMessage = "!cmd",
            clock = clock,
            cooldown = Duration.ofMinutes(10),
            responses = listOf(SendMessage("response"))
        )
        val event = CommandAsked(simpleCommand, User("user"))

        `given now`(now)
        assertThat(simpleCommand.handle(event, channel)).isNotEmpty

        `given now`(now.plusMinutes(15))
        assertThat(simpleCommand.handle(event, channel)).isNotEmpty
    }
}