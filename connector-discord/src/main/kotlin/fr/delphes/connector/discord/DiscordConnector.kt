package fr.delphes.connector.discord

import dev.kord.core.Kord
import dev.kord.core.event.guild.MemberJoinEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.ConnectorStateMachine
import fr.delphes.bot.connector.ConnectorWithStateMachine
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.ConnectionRequested
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.bot.connector.state.DisconnectionRequested
import fr.delphes.bot.connector.state.DisconnectionSuccessful
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.discord.endpoint.DiscordModule
import fr.delphes.connector.discord.incomingEvent.NewGuildMember
import fr.delphes.connector.discord.outgoingEvent.DiscordOutgoingEvent
import fr.delphes.utils.store.Store
import io.ktor.application.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DiscordConnector(
    val bot: Bot,
    override val configFilepath: String
) : Connector, ConnectorWithStateMachine<DiscordConfiguration, DiscordRunTime> {
    private val repository = DiscordConfigurationRepository("${configFilepath}\\discord\\configuration.json")
    private val scope = CoroutineScope(Dispatchers.Default)

    override val stateMachine = ConnectorStateMachine(
        repository = repository,
        doConnection = { configuration ->
            val client = Kord(configuration.oAuthToken)
            client.on<MemberJoinEvent> {
                println(this.member.memberData)
                bot.handleIncomingEvent(NewGuildMember(this.member.displayName))
            }
            client.on<MessageCreateEvent> {
                println("new message ${this.member?.username}")
            }

            val loginJob = scope.launch {
                client.login {
                    @OptIn(PrivilegedIntent::class)
                    intents = Intents.nonPrivileged + Intent.GuildMembers
                }
                println("disconnected discord")
            }
            ConnectionSuccessful(
                configuration,
                DiscordRunTime(client)
            )
        },
        doDisconnect = { configuration, runtime ->
            runtime.client.logout()
            DisconnectionSuccessful(configuration)
        }
    )

    override val states = emptyList<Store<*>>()

    init {
        runBlocking {
            stateMachine.load()
        }
    }

    override suspend fun connect() {
        stateMachine.handle(ConnectionRequested())
    }

    override suspend fun disconnect() {
        stateMachine.handle(DisconnectionRequested())
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
        val currentState = stateMachine.state
        if (currentState is Connected) {
            currentState.runtime.doStuff()
        }
    }

    suspend fun configure(oAuthToken: String) {
        configure(DiscordConfiguration(oAuthToken))
    }
}