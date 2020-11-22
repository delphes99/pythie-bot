package fr.delphes.twitch.payload.pubsub

import kotlinx.serialization.Serializable

@Serializable
class Ping {
    val type = "PING"
}