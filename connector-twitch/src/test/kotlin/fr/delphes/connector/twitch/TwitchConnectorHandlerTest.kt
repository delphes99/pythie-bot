package fr.delphes.connector.twitch

import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.connector.twitch.state.action.MessageReceivedAction
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.utils.store.ActionDispatcher
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class TwitchConnectorHandlerTest {
    private val connector = TwitchConnectorMock()
    private val actionDispatcher = mockk<ActionDispatcher>(relaxed = true)
    private val handler = TwitchConnectorHandler(connector.mock, actionDispatcher)
    private val channel = TwitchChannel("channel")

    @Test
    internal fun `add statistics message received`() {
        runBlocking {
            handler.handle(MessageReceived(channel, User("user"), "message"))
        }

        coVerify(exactly = 1) { actionDispatcher.applyAction(MessageReceivedAction(channel, User("user"), "message")) }
    }
}