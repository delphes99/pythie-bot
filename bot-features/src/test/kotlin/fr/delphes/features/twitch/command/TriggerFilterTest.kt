package fr.delphes.features.twitch.command

import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.features.IncomingEventStub
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test

internal class TriggerFilterTest {
    @Test
    internal fun `false if no command asked`() {
        TriggerFilter("command".toCommand()).isApplicable(IncomingEventStub, NewTwitchCommandState()).shouldBeFalse()

    }

    @Test
    internal fun `true if command matches`() {
        TriggerFilter("command".toCommand()).isApplicable("command".toCommandAsked(), NewTwitchCommandState()).shouldBeTrue()
    }

    @Test
    internal fun `false if command dont matches`() {
        TriggerFilter("command".toCommand()).isApplicable("otherCommand".toCommandAsked(), NewTwitchCommandState()).shouldBeFalse()
    }

    private fun String.toCommandAsked() = CommandAsked(TwitchChannel("channel"), toCommand(), User("user"))

    private fun String.toCommand() = Command(this)
}