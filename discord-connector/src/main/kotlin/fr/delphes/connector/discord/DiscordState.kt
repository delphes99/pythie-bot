package fr.delphes.connector.discord

import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder

sealed class DiscordState {
    object Unconfigured: DiscordState() {
        fun configure(token: String): Configured {
            return Configured(token)
        }
    }

    class Configured(private val token: String): DiscordState() {
        fun connect(): DiscordState {
            return try {
                val client = runBlocking {
                    JDABuilder.createDefault(token).build()
                }

                Connected(client)
            } catch (e: Exception) {
                Error()
            }
        }
    }

    class Error() : DiscordState() {

    }

    class Connected(private val client: JDA): DiscordState() {
        fun send(text: String, channelId: Long) {
            runBlocking {
                val channel = client.getTextChannelById(channelId)!!
                channel.sendMessage(text).complete()
            }
        }
    }
}