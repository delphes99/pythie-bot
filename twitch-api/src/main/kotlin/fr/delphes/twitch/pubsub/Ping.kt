package fr.delphes.twitch.pubsub

import kotlinx.serialization.Serializable

@Serializable
class Ping {
    val type = "PING"
}