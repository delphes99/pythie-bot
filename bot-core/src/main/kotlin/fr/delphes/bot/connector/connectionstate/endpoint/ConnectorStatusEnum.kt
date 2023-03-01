package fr.delphes.bot.connector.connectionstate.endpoint

enum class ConnectorStatusEnum {
    unconfigured,
    configured,
    inError,
    connected,
    connecting,
    disconnecting,
    mixed
}