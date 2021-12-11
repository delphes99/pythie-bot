package fr.delphes.connector.discord

import dev.kord.core.Kord
import dev.kord.core.event.guild.MemberJoinEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import fr.delphes.bot.Bot
import fr.delphes.bot.connector.ConfigurationManager
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.initStateMachine
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.discord.endpoint.DiscordModule
import fr.delphes.connector.discord.incomingEvent.NewGuildMember
import fr.delphes.connector.discord.outgoingEvent.DiscordOutgoingEvent
import io.ktor.application.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscordConnector(
    val bot: Bot,
    override val configFilepath: String
) : Connector<DiscordConfiguration, DiscordRunTime> {
    override val connectorName = "discord"
    override val configurationManager = ConfigurationManager(
        DiscordConfigurationRepository("${configFilepath}\\discord\\configuration.json")
    )
    private val scope = CoroutineScope(Dispatchers.Default)

    override val connectorStateManager = initStateMachine { configuration, _ ->
        val client = Kord(configuration.oAuthToken)
        client.on<MemberJoinEvent> {
            println(this.member.memberData)
            bot.handleIncomingEvent(NewGuildMember(this.member.displayName))
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
    }

    override suspend fun execute(event: OutgoingEvent) {
        if (event is DiscordOutgoingEvent) {
            event.executeOnDiscord(this)
        }
    }

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