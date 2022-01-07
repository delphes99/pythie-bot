package fr.delphes.bot.connector

import fr.delphes.bot.connector.status.ConnectorStatus
import fr.delphes.bot.event.outgoing.OutgoingEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

interface CompositeConnectorStateMachine<CONFIGURATION : ConnectorConfiguration> : ConnectorStateManager<CONFIGURATION> {
    val subStateManagers: List<ConnectorStateManager<CONFIGURATION>>

    override val status: ConnectorStatus
        get() = ConnectorStatus(
            subStateManagers.fold(emptyMap()) { acc, manager ->
                acc + manager.status.subStatus
            }
        )

    override suspend fun handle(command: ConnectorCommand, configurationManager: ConfigurationManager<CONFIGURATION>) {
        coroutineScope {
            subStateManagers.map { sm -> async { sm.handle(command, configurationManager) } }
        }.awaitAll()
    }

    override suspend fun execute(event: OutgoingEvent) {
        subStateManagers.forEach { sm ->
            sm.execute(event)
        }
    }
}