package fr.delphes.overlay

import fr.delphes.bot.connector.ConnectorRuntime

class OverlayRuntime : ConnectorRuntime {
    override suspend fun kill() {}
}
