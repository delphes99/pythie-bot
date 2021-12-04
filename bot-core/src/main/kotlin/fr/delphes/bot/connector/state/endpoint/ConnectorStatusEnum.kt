package fr.delphes.bot.connector.state.endpoint

enum class ConnectorStatusEnum {
    unconfigured,
    configured,
    inError,
    connected,
    connecting,
    disconnecting
}