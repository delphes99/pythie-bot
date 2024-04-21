package fr.delphes.connector.twitch.command

import fr.delphes.bot.Bot
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import fr.delphes.utils.time.Clock
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import java.time.Duration
import java.time.LocalDateTime

class SimpleCommandHandlerTest : ShouldSpec({
    val clock = mockk<Clock>()
    val bot = mockk<Bot>()

    fun `given now`(now: LocalDateTime) {
        every { clock.now() } returns now
    }

    beforeTest {
        clearAllMocks()

        `given now`(now)
    }

    should("trigger command") {
        val command = Command("!cmd")
        val simpleCommand = SimpleCommandHandler(
            channel = channel,
            command = command,
            clock = clock,
            responses = {
                listOf(SendMessage(channel, "response"))
            }
        )
        val event = CommandAsked(channel, command, UserName("user"))

        simpleCommand.handle(event, bot).shouldContainExactly(
            SendMessage(channel, "response")
        )
    }

    should("don't trigger command if too frequent") {
        val command = Command("!cmd")
        val simpleCommand = SimpleCommandHandler(
            channel = channel,
            command = command,
            clock = clock,
            cooldown = Duration.ofMinutes(10),
            responses = {
                listOf(SendMessage(channel, "response"))
            }
        )
        val event = CommandAsked(channel, command, UserName("user"))

        `given now`(now)
        simpleCommand.handle(event, bot).shouldNotBeEmpty()

        `given now`(now.plusMinutes(5))
        simpleCommand.handle(event, bot).shouldBeEmpty()
    }

    should("trigger command if after cooldown") {
        val command = Command("!cmd")
        val simpleCommand = SimpleCommandHandler(
            channel = channel,
            command = command,
            clock = clock,
            cooldown = Duration.ofMinutes(10),
            responses = {
                listOf(SendMessage(channel, "response"))
            }
        )
        val event = CommandAsked(channel, command, UserName("user"))

        `given now`(now)
        simpleCommand.handle(event, bot).shouldNotBeEmpty()

        `given now`(now.plusMinutes(15))
        simpleCommand.handle(event, bot).shouldNotBeEmpty()
    }
}) {
    companion object {
        private val now = LocalDateTime.of(2020, 1, 1, 0, 0)
        private val channel = TwitchChannel("channel")
    }
}