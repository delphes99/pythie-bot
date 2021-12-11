package fr.delphes.connector.twitch.eventMapper

import fr.delphes.bot.state.ChannelStatistics
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcChannelMessage
import fr.delphes.twitch.irc.IrcUser
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ChannelMessageMapperTest {
    private val statistics = mockk<ChannelStatistics>(relaxed = true)
    private val connector = mockk<TwitchConnector>()
    private val botName = "botAccount"
    private val userName = "user"
    private val message = "message"
    private val channelName = "channel"
    private val channel = TwitchChannel(channelName)

    private val mapper = ChannelMessageMapper(channel, connector)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        every { connector.commandsFor(channel) } returns emptyList()
        every { connector.statistics.of(channel) } returns statistics
        every { connector.botAccount } returns TwitchChannel(botName)
    }

    @Test
    internal fun `message from viewer`() {
        val twitchEvent = IrcChannelMessage(IrcChannel.withName(channelName), IrcUser(userName), message)

        val events = runBlocking {
            mapper.handle(twitchEvent)
        }

        events shouldHaveSingleElement MessageReceived(channel, userName, message)
    }

    @Test
    internal fun `message from bot should not trigger event`() {
        val twitchEvent = IrcChannelMessage(IrcChannel.withName(channelName), IrcUser(botName), message)

        val events = runBlocking {
            mapper.handle(twitchEvent)
        }

        events.shouldBeEmpty()
    }
}