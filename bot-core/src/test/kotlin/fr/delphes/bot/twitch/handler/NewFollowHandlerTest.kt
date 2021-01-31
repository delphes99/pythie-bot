package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelFollow.NewFollow
import fr.delphes.twitch.api.user.User
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NewFollowHandlerTest {
    private val changeState = mockk<ChannelChangeState>(relaxed = true)
    private val channel = mockk<ChannelInfo>()
    private val CHANNEL = TwitchChannel("channel")

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    internal fun `add follow statistics`() {
        val event = "user".follows()
        runBlocking {
            NewFollowHandler().handle(event, channel, changeState)
        }

        coVerify(exactly = 1) { changeState.newFollow(User("user")) }
    }

    private fun String.follows(): NewFollow {
        return NewFollow(CHANNEL, User(this))
    }
}