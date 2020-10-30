package fr.delphes.bot.twitch.handler

import fr.delphes.User
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.webserver.payload.newFollow.NewFollowData
import fr.delphes.bot.webserver.payload.newFollow.NewFollowPayload
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class NewFollowHandlerTest {
    private val changeState = mockk<ChannelChangeState>(relaxed = true)
    private val channel = mockk<ChannelInfo>()

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    internal fun `add follow statistics`() {
        val event = "user".follows()
        NewFollowHandler().handle(event, channel, changeState)

        verify(exactly = 1) { changeState.newFollow(User("user")) }
    }

    private fun String.follows(): NewFollowPayload {
        val followed_at = LocalDateTime.of(2020, 1, 1, 12, 0)
        return NewFollowPayload(NewFollowData("id", this, "streamId", "streamer", followed_at))
    }
}