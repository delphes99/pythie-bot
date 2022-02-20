package fr.delphes.connector.twitch.feature

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class ChannelFilterTest {
    @Test
    internal fun `match only twitch event`() {
        val genericEvent = mockk<IncomingEvent>()

        ChannelFilter(CHANNEL).isApplicable(genericEvent).shouldBeFalse()
    }

    @Test
    internal fun `match channel`() {
        val event = buildIncomingEventFor(CHANNEL)

        ChannelFilter(CHANNEL).isApplicable(event).shouldBeTrue()
    }

    @Test
    internal fun `don't match channel`() {
        val event = buildIncomingEventFor(OTHER_CHANNEL)

        ChannelFilter(CHANNEL).isApplicable(event).shouldBeFalse()
    }

    private fun buildIncomingEventFor(channel: TwitchChannel): TwitchIncomingEvent {
        val event = mockk<TwitchIncomingEvent>()
        every { event.channel } returns channel
        //TODO don't use mock
        every { event.isFor(any()) } returns false
        every { event.isFor(channel) } returns true
        return event
    }

    companion object {
        private val CHANNEL = TwitchChannel("channel")
        private val OTHER_CHANNEL = TwitchChannel("other channel")
    }
}