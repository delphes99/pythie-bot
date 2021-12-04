package fr.delphes.connector.obs

import fr.delphes.bot.connector.ConnectorRuntime
import fr.delphes.obs.ObsClient

class ObsRunTime(val client: ObsClient) : ConnectorRuntime {
    override suspend fun kill() {
        client.disconnect()
    }
}
