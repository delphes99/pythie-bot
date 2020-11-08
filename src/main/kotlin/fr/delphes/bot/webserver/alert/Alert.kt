package fr.delphes.bot.webserver.alert

import kotlinx.serialization.Serializable

@Serializable
data class Alert(val text: String)