package fr.delphes.connector.twitch.eventMapper

import fr.delphes.bot.state.ChannelStatistics
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcChannelMessage
import fr.delphes.twitch.irc.IrcUser
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk

class ChannelMessageMapperTest : ShouldSpec({
    val statistics = mockk<ChannelStatistics>(relaxed = true)
    val connector = mockk<TwitchConnector>()

    val mapper = ChannelMessageMapper(channel, connector)

    beforeTest {
        clearAllMocks()

        every { connector.commandsFor(channel) } returns emptyList()
        every { connector.statistics.of(channel) } returns statistics
        every { connector.botAccount } returns TwitchChannel(botName)
    }

    should("message from viewer") {
        val twitchEvent = IrcChannelMessage(IrcChannel.withName(channelName), IrcUser(userName), message)

        val events = mapper.handle(twitchEvent)

        events shouldHaveSingleElement MessageReceived(channel, userName, message)
    }

    should("message from bot should not trigger event") {
        val twitchEvent = IrcChannelMessage(IrcChannel.withName(channelName), IrcUser(botName), message)

        val events = mapper.handle(twitchEvent)

        events.shouldBeEmpty()
    }
}) {
    companion object {
        private const val botName = "botAccount"
        private const val userName = "user"
        private const val message = "message"
        private const val channelName = "channel"
        private val channel = TwitchChannel(channelName)
    }
}