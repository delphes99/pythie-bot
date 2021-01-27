package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.state.UserMessage
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
import org.junit.jupiter.api.Test

internal class ChannelMessageHandlerTest {
    private val changeState = mockk<ChannelChangeState>(relaxed = true)
    private val channel = mockk<ChannelInfo>()

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        every { channel.commands } returns emptyList()
    }

    @Test
    internal fun `add statistics message received`() {
        runBlocking {
            ChannelMessageHandler().handle(
                IrcChannelMessage(IrcChannel("myChannel"), IrcUser("user"), "message"),
                channel,
                changeState
            )
        }

        coVerify(exactly = 1) { changeState.addMessage(UserMessage(User("user"), "message")) }
    }
}