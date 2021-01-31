package fr.delphes.connector.discord

import fr.delphes.connector.discord.api.JoinListener
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent

sealed class DiscordState {
    object Unconfigured : DiscordState() {
        fun configure(token: String, connector: DiscordConnector): Configured {
            return Configured(token, connector)
        }
    }

    class Configured(
        private val token: String,
        private val connector: DiscordConnector
    ) : DiscordState() {
        fun connect(): DiscordState {
            return try {
                val client = runBlocking {
                    JDABuilder
                        .createDefault(token, GatewayIntent.GUILD_MEMBERS)
                        .build()
                }

                client.addEventListener(JoinListener(connector))

                Connected(client)
            } catch (e: Exception) {
                LOGGER.error(e) { "Discord connection failed" }
                Error
            }
        }
    }

    object Error : DiscordState()

    class Connected(internal val client: JDA) : DiscordState()

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}