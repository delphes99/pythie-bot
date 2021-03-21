package fr.delphes.connector.twitch.eventMapper

import fr.delphes.bot.state.ChannelState
import fr.delphes.bot.state.UserMessage
import fr.delphes.connector.twitch.ClientBot
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcChannelMessage
import fr.delphes.twitch.irc.IrcUser
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

//TODO make it work when move to connector implementation
@Disabled("move state change outside the mapper")
internal class ChannelMessageMapperTest {
    private val changeState = mockk<ChannelState>(relaxed = true)
    private val channel = TwitchChannel("channel")
    private val clientBot = mockk<ClientBot>()

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        every { clientBot.commandsFor(channel) } returns emptyList()
        every { clientBot.channelOf(channel)?.state } returns changeState
    }


    @Test
    internal fun `add statistics message received`() {
        runBlocking {
            ChannelMessageMapper(channel, clientBot).handle(
                IrcChannelMessage(IrcChannel.withName("channel"), IrcUser("user"), "message")
            )
        }

        coVerify(exactly = 1) { changeState.addMessage(UserMessage(User("user"), "message")) }
    }
}