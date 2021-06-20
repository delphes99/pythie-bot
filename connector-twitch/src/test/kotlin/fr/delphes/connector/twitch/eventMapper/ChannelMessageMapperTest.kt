package fr.delphes.connector.twitch.eventMapper

import fr.delphes.bot.state.ChannelState
import fr.delphes.bot.state.UserMessage
import fr.delphes.connector.twitch.ClientBot
import fr.delphes.connector.twitch.state.action.MessageReceivedAction
import fr.delphes.connector.twitch.state.mockBotAccountProvider
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcChannelMessage
import fr.delphes.twitch.irc.IrcUser
import fr.delphes.utils.store.Action
import io.kotest.matchers.shouldBe
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
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
        shouldSendAction(
            expected = MessageReceivedAction(channel, User("user"), "message")
        ) { applyAction ->
            runBlocking {
                ChannelMessageMapper(
                    channel,
                    clientBot,
                    mockBotAccountProvider("channel"),
                    applyAction
                ).handle(
                    IrcChannelMessage(IrcChannel.withName("channel"), IrcUser("user"), "message")
                )
            }
        }

        //TODO Delete
        coVerify(exactly = 1) { changeState.addMessage(UserMessage(User("user"), "message")) }
    }

    private fun shouldSendAction(expected: MessageReceivedAction, doStuff: ((Action) -> Unit) -> Unit) {
        val (applyAction, captureAction) = createSlot()

        doStuff(applyAction)

        captureAction.captured shouldBe expected
    }

    private fun createSlot(): Pair<(Action) -> Unit, CapturingSlot<Action>> {
        val applyAction: (Action) -> Unit = mockk()
        val captureAction = slot<Action>()

        coEvery { applyAction.invoke(capture(captureAction)) } just Runs

        return applyAction to captureAction
    }
}