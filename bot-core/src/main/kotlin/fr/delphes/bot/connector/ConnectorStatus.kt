package fr.delphes.bot.connector

enum class ConnectorStatus {
    Configured,
    Connected,
    Connecting,
    Disconnecting,
    InError,
    NotConfigured,
}