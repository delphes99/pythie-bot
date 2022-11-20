package fr.delphes.connector.discord

import dev.kord.core.Kord
import dev.kord.core.event.guild.MemberJoinEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.SimpleConfigurationManager
import fr.delphes.bot.connector.initStateMachine
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.connector.discord.endpoint.DiscordModule
import fr.delphes.connector.discord.incomingEvent.NewGuildMember
import fr.delphes.connector.discord.outgoingEvent.DiscordOutgoingEvent
import io.ktor.server.application.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscordConnector(
    val bot: Bot,
    override val configFilepath: String
) : Connector<DiscordConfiguration, DiscordRunTime> {
    override val connectorName = "Discord"
    override val configurationManager = SimpleConfigurationManager(
        DiscordConfigurationRepository("${configFilepath}\\discord\\configuration.json")
    )
    private val scope = CoroutineScope(Dispatchers.Default)

    override val connectorStateManager = initStateMachine<DiscordConfiguration, DiscordRunTime>(
        connectionName = "Kord",
        doConnection = { configuration, _ ->
            val client = Kord(configuration.oAuthToken)
            client.on<MemberJoinEvent> {
                println(this.member.memberData)
                bot.handle(NewGuildMember(this.member.displayName))
            }

            scope.launch {
                client.login {
                    @OptIn(PrivilegedIntent::class)
                    intents = Intents.nonPrivileged + Intent.GuildMembers
                }
            }
            ConnectionSuccessful(
                configuration,
                DiscordRunTime(client)
            )
        },
        executeEvent = { event ->
            if (event is DiscordOutgoingEvent) {
                event.executeOnDiscord(this@DiscordConnector)
            }
        },
        configurationManager = configurationManager,
    )

    override fun internalEndpoints(application: Application) {
        return application.DiscordModule(this)
    }

    override fun publicEndpoints(application: Application) {
    }

    suspend fun connected(doStuff: suspend DiscordRunTime.() -> Unit) {
        val currentState = connectorStateManager.state
        if (currentState is Connected) {
            currentState.runtime.doStuff()
        }
    }
}