package fr.delphes.bot.connector

import fr.delphes.bot.connector.status.ConnectorStatus
import fr.delphes.bot.event.outgoing.OutgoingEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

interface CompositeConnectionManager<CONFIGURATION : ConnectorConfiguration> : ConnectionManager<CONFIGURATION> {
    val subConnectionManagers: List<ConnectionManager<CONFIGURATION>>

    override val status: ConnectorStatus
        get() = ConnectorStatus(
            subConnectionManagers.fold(emptyMap()) { acc, manager ->
                acc + manager.status.subStatus
            }
        )

    override suspend fun dispatchTransition(command: ConnectorCommand) {
        coroutineScope {
            subConnectionManagers.map { sm -> async { sm.dispatchTransition(command) } }
        }.awaitAll()
    }

    override suspend fun execute(event: OutgoingEvent) {
        subConnectionManagers.forEach { sm ->
            sm.execute(event)
        }
    }
}