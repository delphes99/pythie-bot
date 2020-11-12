package fr.delphes.bot.twitch.handler

import fr.delphes.User
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.webserver.payload.newSub.NewSubData
import fr.delphes.bot.webserver.payload.newSub.NewSubEventData
import fr.delphes.bot.webserver.payload.newSub.NewSubPayload
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class NewSubHandlerTest {
    private val changeState = mockk<ChannelChangeState>(relaxed = true)
    private val channel = mockk<ChannelInfo>()

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    internal fun `add follow statistics`() {
        val event = "user".subscribe()
        NewSubHandler().handle(event, channel, changeState)

        verify(exactly = 1) { changeState.newSub(User("user")) }
    }

    private fun String.subscribe(): NewSubPayload {
        val time = LocalDateTime.of(2020, 1, 1, 12, 0)
        val data = mockk<NewSubEventData>()
        every { data.user_name } returns this
        return NewSubPayload(NewSubData("id", "this", time, 1.0, data))
    }
}