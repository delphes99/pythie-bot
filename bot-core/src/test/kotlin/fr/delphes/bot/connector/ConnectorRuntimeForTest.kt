package fr.delphes.bot.connector

object ConnectorRuntimeForTest: ConnectorRuntime {
    override suspend fun kill() {}
}