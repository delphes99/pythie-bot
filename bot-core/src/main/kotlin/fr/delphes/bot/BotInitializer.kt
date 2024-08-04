package fr.delphes.bot

import fr.delphes.bot.connector.Connector
import fr.delphes.state.StateManager
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class BotInitializer(
    val stateManager: StateManager,
    val connectors: List<Connector<*, *>>,
) {
    suspend fun initialize() {
        coroutineScope {
            connectors.map { connector -> async { connector.connect() } }.awaitAll()
            
            connectors.forEach { connector ->
                (connector.states + connector.enumerationStates).forEach(stateManager::put)
            }
        }
    }
}