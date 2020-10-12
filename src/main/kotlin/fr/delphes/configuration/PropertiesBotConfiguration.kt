package fr.delphes.configuration

class PropertiesBotConfiguration(
) : BotConfiguration {
    override val clientId: String
    override val secretKey: String
    override val botAccountOauth: String

    init {
        val properties = loadProperties("configuration-pythiebot.properties")
        clientId = properties.getProperty("client.id")
        secretKey = properties.getProperty("secret.key")
        botAccountOauth = properties.getProperty("bot.account.OAuth")
    }
}