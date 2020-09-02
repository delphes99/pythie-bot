package fr.delphes.configuration

interface Configuration {
    val clientId: String
    val secretKey: String
    val ownerAccountOauth: String
    val ownerChannel: String
    val ownerChannelId : String
    val botAccountOauth: String
}