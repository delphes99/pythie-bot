package fr.delphes.connector.twitch

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot

class TwitchConnectorMock {
    val mock: TwitchConnector = mockk()
    private val clientBot: ClientBot = mockk()
    private val connectedState = TwitchState.AppConnected(clientBot)

    init {
        val slot = slot<(suspend TwitchState.AppConnected.() -> Unit)>()
        coEvery {
            mock.whenRunning(capture(slot))
        } coAnswers {
            slot.captured.invoke(connectedState)
        }
    }
}