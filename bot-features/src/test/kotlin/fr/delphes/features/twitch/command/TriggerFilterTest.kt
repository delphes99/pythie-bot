package fr.delphes.features.twitch.command

import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.features.IncomingEventStub
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

internal class TriggerFilterTest : ShouldSpec({
    should("false if no command asked") {
        TriggerFilter("command".toCommand()).isApplicable(IncomingEventStub, NewTwitchCommandState()).shouldBeFalse()

    }

    should("true if command matches") {
        TriggerFilter("command".toCommand()).isApplicable("command".toCommandAsked(), NewTwitchCommandState()).shouldBeTrue()
    }

    should("false if command dont matches") {
        TriggerFilter("command".toCommand()).isApplicable("otherCommand".toCommandAsked(), NewTwitchCommandState()).shouldBeFalse()
    }
}) {
    companion object {
        private fun String.toCommandAsked() = CommandAsked(TwitchChannel("channel"), toCommand(), User("user"))

        private fun String.toCommand() = Command(this)
    }
}