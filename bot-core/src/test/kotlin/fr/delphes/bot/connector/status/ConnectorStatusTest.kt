package fr.delphes.bot.connector.status

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class ConnectorStatusTest : ShouldSpec({
    should("one connection returns the status of the connection") {
        val connectorStatus = ConnectorStatus(
            "connection" to ConnectorConnectionStatus.Connected
        )

        connectorStatus.status shouldBe ConnectorConnectionStatus.Connected
    }

    should("two connections with the same status should return the status") {
        val connectorStatus = ConnectorStatus(
            "connection 1" to ConnectorConnectionStatus.InError,
            "connection 2" to ConnectorConnectionStatus.InError,
        )

        connectorStatus.status shouldBe ConnectorConnectionStatus.InError
    }

    should("two connections with different status should return mixed status") {
        val connectorStatus = ConnectorStatus(
            "connection 1" to ConnectorConnectionStatus.Connected,
            "connection 2" to ConnectorConnectionStatus.InError,
        )

        connectorStatus.status shouldBe ConnectorConnectionStatus.Mixed
    }
})