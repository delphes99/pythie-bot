package fr.delphes.features.twitch.command

import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.features.atFixedTime
import fr.delphes.features.hasReceived
import fr.delphes.rework.feature.FeatureId
import fr.delphes.state.StateId
import fr.delphes.state.state.TimeState
import fr.delphes.test.TestClock
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.LocalDateTime

class CustomCommandTest : ShouldSpec({
    should("should register a command message handler") {
        var isCalled = false

        val command = CustomCommand(CHANNEL, "!trigger") { isCalled = true }

        command.hasReceived(
            CommandAsked(
                CHANNEL,
                Command("!trigger"),
                User("user")
            )
        )

        isCalled shouldBe true
    }

    should("not call action if trigger does not match") {
        var isCalled = false


        val command = CustomCommand(CHANNEL, "!trigger") { isCalled = true }

        command.hasReceived(
            CommandAsked(
                CHANNEL,
                Command("!othertrigger"),
                User("user")
            )
        )

        isCalled shouldBe false
    }

    should("not call action if channel does not match") {
        var isCalled = false
        val command = CustomCommand(CHANNEL, "!trigger") { isCalled = true }

        command.hasReceived(
            CommandAsked(
                TwitchChannel("otherchannel"),
                Command("!trigger"),
                User("user")
            )
        )

        isCalled shouldBe false
    }

    should("not call action if previous call is too recent") {
        var isCalled = false

        val command = CustomCommand(
            CHANNEL,
            "!trigger",
            FeatureId(FEATURE_ID),
            Duration.ofMinutes(2)
        ) { isCalled = true }

        command
            .atFixedTime(NOW)
            .withState(TimeState(StateId(FEATURE_ID), NOW.minusMinutes(1), TestClock(NOW)))
            .hasReceived(
                CommandAsked(
                    CHANNEL,
                    Command("!trigger"),
                    User("user")
                )
            )

        isCalled shouldBe false
    }

    should("call action if the cooldown is over") {
        var isCalled = false

        val command = CustomCommand(
            CHANNEL,
            "!trigger",
            FeatureId(FEATURE_ID),
            Duration.ofMinutes(2)
        ) { isCalled = true }

        command
            .atFixedTime(NOW)
            .withState(TimeState(StateId(FEATURE_ID), NOW.minusMinutes(3), TestClock(NOW)))
            .hasReceived(
                CommandAsked(
                    CHANNEL,
                    Command("!trigger"),
                    User("user")
                )
            )

        isCalled shouldBe true
    }
}) {
    companion object {
        private val NOW = LocalDateTime.of(2021, 1, 1, 0, 0, 0)
        private val CHANNEL = TwitchChannel("channel")
        private const val FEATURE_ID = "featureId"
    }
}
