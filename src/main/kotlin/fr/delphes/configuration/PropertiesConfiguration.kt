package fr.delphes.configuration

import java.util.Properties

class PropertiesConfiguration(
) : Configuration {
    override val clientId: String
    override val secretKey: String
    override val ownerAccountOauth: String
    override val ownerChannel: String
    override val ownerChannelId: String
    override val botAccountOauth: String

    init {
        val properties = Properties()
        properties.load(javaClass.classLoader.getResourceAsStream("./configuration.properties"))
        clientId = properties.getProperty("client.id")
        secretKey = properties.getProperty("secret.key")
        ownerAccountOauth = properties.getProperty("owner.account.OAuth")
        ownerChannel = properties.getProperty("owner.channel.name")
        ownerChannelId = properties.getProperty("owner.channel.id")
        botAccountOauth = properties.getProperty("bot.account.OAuth")
    }
}