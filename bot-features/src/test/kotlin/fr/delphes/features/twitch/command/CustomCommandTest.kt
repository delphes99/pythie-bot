package fr.delphes.features.twitch.command

import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class CustomCommandTest : ShouldSpec({
    val CHANNEL = TwitchChannel("channel")

    should("should register a command message handler") {
        var isCalled = false
        val runtime = CustomCommand(CHANNEL, "!trigger") { isCalled = true }
            .buildRuntime()

        runtime.handleIncomingEvent(
            CommandAsked(
                CHANNEL,
                Command("!trigger"),
                User("user")
            ),
            mockk()
        )

        isCalled shouldBe true
    }

    should("not call action if trigger does not match") {
        var isCalled = false
        val runtime = CustomCommand(CHANNEL, "!trigger") { isCalled = true }
            .buildRuntime()

        runtime.handleIncomingEvent(
            CommandAsked(
                CHANNEL,
                Command("!othertrigger"),
                User("user")
            ),
            mockk()
        )

        isCalled shouldBe false
    }

    should("not call action if channel does not match") {
        var isCalled = false
        val runtime = CustomCommand(CHANNEL, "!trigger") { isCalled = true }
            .buildRuntime()

        runtime.handleIncomingEvent(
            CommandAsked(
                TwitchChannel("otherchannel"),
                Command("!trigger"),
                User("user")
            ),
            mockk()
        )

        isCalled shouldBe false
    }
})
