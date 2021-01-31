package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelSubscribe.NewSub
import fr.delphes.twitch.api.channelSubscribe.SubscribeTier
import fr.delphes.twitch.api.user.User
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NewSubHandlerTest {
    private val changeState = mockk<ChannelChangeState>(relaxed = true)
    private val channel = mockk<ChannelInfo>()
    private val CHANNEL = TwitchChannel("channel")

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    internal fun `add follow statistics`() {
        val event = "user".subscribe()

        runBlocking {
            NewSubHandler().handle(event, channel, changeState)
        }

        coVerify(exactly = 1) { changeState.newSub(User("user")) }
    }

    private fun String.subscribe(): NewSub {
        return NewSub(CHANNEL, User(this), SubscribeTier.TIER_1, false)
    }
}