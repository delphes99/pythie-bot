package fr.delphes.bot.connector.state.endpoint

import fr.delphes.bot.connector.status.ConnectorConnectionStatus
import fr.delphes.bot.connector.status.ConnectorStatus
import fr.delphes.bot.connector.status.toOutput
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class ConnectorStatusOutputTest : ShouldSpec({
    should("one connection returns the status of the connection") {
        val connectorStatus = ConnectorStatus(
            mapOf(
                "connection successful" to ConnectorConnectionStatus.Connected,
                "connection failed" to ConnectorConnectionStatus.InError,
            )
        )

        toOutput("name", connectorStatus) shouldBe ConnectorStatusOutput(
            "name",
            ConnectorStatusEnum.mixed,
            listOf(
                ConnectorStatusOutput(
                    "connection successful",
                    ConnectorStatusEnum.connected
                ),
                ConnectorStatusOutput(
                    "connection failed",
                    ConnectorStatusEnum.inError
                ),
            )
        )
    }
})
