package fr.delphes.bot.connector.status

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ConnectorStatusTest {
    @Test
    internal fun `one connection returns the status of the connection`() {
        val connectorStatus = ConnectorStatus(
            "connection" to ConnectorConnectionStatus.Connected
        )

        connectorStatus.status shouldBe ConnectorConnectionStatus.Connected
    }

    @Test
    internal fun `two connections with the same status should return the status`() {
        val connectorStatus = ConnectorStatus(
            "connection 1" to ConnectorConnectionStatus.InError,
            "connection 2" to ConnectorConnectionStatus.InError,
        )

        connectorStatus.status shouldBe ConnectorConnectionStatus.InError
    }

    @Test
    internal fun `two connections with different status should return mixed status`() {
        val connectorStatus = ConnectorStatus(
            "connection 1" to ConnectorConnectionStatus.Connected,
            "connection 2" to ConnectorConnectionStatus.InError,
        )

        connectorStatus.status shouldBe ConnectorConnectionStatus.Mixed
    }
}