package fr.delphes.connector.twitch.eventMapper

import fr.delphes.bot.state.ChannelState
import fr.delphes.connector.twitch.ClientBot
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelFollow.NewFollow
import fr.delphes.twitch.api.user.User
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
internal class NewFollowMapperTest {
    private val changeState = mockk<ChannelState>(relaxed = true)
    private val bot = mockk<ClientBot>()
    private val connector = mockk<TwitchConnector>()
    private val channel = TwitchChannel("channel")

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        every { bot.channelOf(channel)?.state } returns changeState
    }

    @Test
    internal fun `add follow statistics`() {
        val event = "user".follows()

        runBlocking {
            NewFollowMapper(connector).handle(event)
        }

        coVerify(exactly = 1) { changeState.newFollow(User("user")) }
    }

    private fun String.follows(): NewFollow {
        return NewFollow(channel, User(this))
    }
}