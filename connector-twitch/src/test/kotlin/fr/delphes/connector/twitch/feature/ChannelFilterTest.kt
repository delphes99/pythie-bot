package fr.delphes.connector.twitch.feature

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.feature.featureNew.NoState
import fr.delphes.twitch.TwitchChannel
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.mockk

class ChannelFilterTest : ShouldSpec({
    should("match only twitch event") {
        val genericEvent = mockk<IncomingEvent>()

        ChannelFilter<NoState>(CHANNEL).isApplicable(genericEvent, NoState).shouldBeFalse()
    }

    should("match channel") {
        val event = incomingEventOf(CHANNEL)

        ChannelFilter<NoState>(CHANNEL).isApplicable(event, NoState).shouldBeTrue()
    }

    should("don't match channel") {
        val event = incomingEventOf(OTHER_CHANNEL)

        ChannelFilter<NoState>(CHANNEL).isApplicable(event, NoState).shouldBeFalse()
    }
}) {
    companion object {
        private val CHANNEL = TwitchChannel("channel")
        private val OTHER_CHANNEL = TwitchChannel("other channel")

        fun incomingEventOf(channel: TwitchChannel): TwitchIncomingEvent {
            return MessageReceived(channel, "user", "message")
        }
    }
}