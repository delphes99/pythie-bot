package fr.delphes.configuration

interface BotConfiguration {
    val clientId: String
    val secretKey: String
    val botAccountOauth: String
    val webhookSecret: String
    val discordOAuth: String
}