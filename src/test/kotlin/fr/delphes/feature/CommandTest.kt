package fr.delphes.feature

import fr.delphes.event.incoming.MessageReceived
import fr.delphes.event.outgoing.SendMessage
import fr.delphes.feature.command.Command
import fr.delphes.time.Clock
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

internal class CommandTest {
    private val clock = mockk<Clock>()
    private val now = LocalDateTime.of(2020, 1, 1, 0, 0)

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
        val event = MessageReceived("user", "!cmd")
        val messages = Command(
            "!cmd",
            "response",
            clock = clock
        ).CommandMessageReceivedHandler().handle(event)

        assertThat(messages).containsExactlyInAnyOrder(
            SendMessage("response")
        )
    }

    @Test
    internal fun `don't trigger command`() {
        val command = Command(
            trigger = "!otherCmd",
            response = "response",
            clock = clock
        )
        val event = MessageReceived("user", "!cmd")
        val messages = command.CommandMessageReceivedHandler().handle(event)

        assertThat(messages).isEmpty()
    }

    @Test
    internal fun `don't trigger command if too frequent`() {
        val event = MessageReceived("user", "!cmd")
        val handler = Command(
            trigger = "!cmd",
            response = "response",
            cooldown = Duration.ofMinutes(10),
            clock = clock
        ).CommandMessageReceivedHandler()

        `given now`(now)
        val messages = handler.handle(event)
        assertThat(messages).isNotEmpty()

        `given now`(now.plusMinutes(5))
        val messageBeforeCooldownFaded = handler.handle(event)
        assertThat(messageBeforeCooldownFaded).isEmpty()
    }

    @Test
    internal fun `trigger command if after cooldown`() {
        val event = MessageReceived("user", "!cmd")
        val handler = Command(
            trigger = "!cmd",
            response = "response",
            cooldown = Duration.ofMinutes(10),
            clock = clock
        ).CommandMessageReceivedHandler()

        `given now`(now)
        val messages = handler.handle(event)
        assertThat(messages).isNotEmpty()

        `given now`(now.plusMinutes(15))
        val messageAfterCooldownFaded = handler.handle(event)
        assertThat(messageAfterCooldownFaded).isNotEmpty()
    }
}