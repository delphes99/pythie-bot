package fr.delphes.connector.twitch.state.reducer

import fr.delphes.bot.state.UserMessage
import fr.delphes.connector.twitch.state.TwitchChannelState
import fr.delphes.connector.twitch.state.TwitchConnectorState
import fr.delphes.connector.twitch.state.action.MessageReceivedAction
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class MessageReceivedReducerTest {
    private val channel1 = TwitchChannel("channel1")

    @Test
    internal fun initialState() {
        TwitchConnectorState().stateOf(channel1).userMessages shouldBe emptyList()
    }

    @Test
    internal fun `add message`() {
        val initialState = TwitchConnectorState(
            channel1 to TwitchChannelState(userMessages = listOf(UserMessage(User("some user"), "old message")))
        )
        val action = MessageReceivedAction(channel1, User("user"), "message")

        val newState = messageReceivedReducer.apply(action, initialState)

        newState.stateOf(channel1).userMessages shouldContainInOrder listOf(
            UserMessage(User("some user"), "old message"),
            UserMessage(User("user"), "message")
        )
    }
}