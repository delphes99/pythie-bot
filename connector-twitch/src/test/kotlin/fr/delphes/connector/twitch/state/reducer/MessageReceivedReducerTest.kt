package fr.delphes.connector.twitch.state.reducer

import fr.delphes.bot.state.UserMessage
import fr.delphes.connector.twitch.state.TwitchConnectorState
import fr.delphes.connector.twitch.state.action.MessageReceivedAction
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class MessageReceivedReducerTest {
    @Test
    internal fun initialState() {
        TwitchConnectorState().userMessages shouldBe emptyList()
    }

    @Test
    internal fun `add message`() {
        val initialState = TwitchConnectorState(userMessages = listOf(UserMessage(User("some user"), "old message")))
        val action = MessageReceivedAction(TwitchChannel("myChannel"), User("user"), "message")

        val newState = MessageReceivedReducer().applyOn(initialState, action)

        newState.userMessages shouldContainInOrder listOf(
            UserMessage(User("some user"), "old message"),
            UserMessage(User("user"), "message")
        )
    }
}