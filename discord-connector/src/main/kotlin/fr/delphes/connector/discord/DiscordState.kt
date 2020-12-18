package fr.delphes.connector.discord

import dev.kord.core.Kord
import kotlinx.coroutines.runBlocking

sealed class DiscordState {
    object Unconfigured: DiscordState() {
        fun configure(token: String): Configured {
            return Configured(token)
        }
    }

    class Configured(private val token: String): DiscordState() {
        fun connect(): Connected {
            print("connect")
            val client = runBlocking {
                Kord(token)
            }
            print("connected")

            return Connected(client)
        }
    }

    class Error() : DiscordState() {

    }

    class Connected(private val client: Kord): DiscordState() {

    }
}