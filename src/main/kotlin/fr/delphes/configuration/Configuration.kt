package fr.delphes.configuration

interface Configuration {
    val clientId: String
    val secretKey: String
    val ownerAccountOauth: String
    val botAccountOauth: String
}