package fr.delphes.bot.twitch.handler

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.common.events.domain.EventUser
import fr.delphes.User
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.state.UserMessage
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ChannelMessageHandlerTest {
    private val changeState = mockk<ChannelChangeState>(relaxed = true)
    private val twitchEvent = mockk<ChannelMessageEvent>()
    private val channel = mockk<ChannelInfo>()

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        every { channel.commands } returns emptyList()
    }

    @Test
    internal fun `add statistics message received`() {
        "user".sends("message")

        ChannelMessageHandler().handle(
            twitchEvent,
            channel,
            changeState
        )

        verify(exactly = 1) { changeState.addMessage(UserMessage(User("user"), "message")) }
    }

    private fun String.sends(message: String) {
        every { twitchEvent.user } returns EventUser("id", this)
        every { twitchEvent.message } returns message
    }
}