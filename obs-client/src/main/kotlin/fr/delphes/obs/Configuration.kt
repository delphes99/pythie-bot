package fr.delphes.obs

data class Configuration(
    val host: String,
    val port: Int,
    val password: String?,
)