package fr.delphes.bot.connector

interface ConnectorRuntime {
    suspend fun kill()
}