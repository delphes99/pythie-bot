package fr.delphes.bot.connector.status

enum class ConnectorConnectionStatus {
    Configured,
    Connected,
    Connecting,
    Disconnecting,
    InError,
    NotConfigured,
    Mixed
}