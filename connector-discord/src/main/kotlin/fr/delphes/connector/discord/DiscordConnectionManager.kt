package fr.delphes.connector.discord

import dev.kord.core.Kord
import dev.kord.core.event.guild.MemberJoinEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import fr.delphes.bot.connector.StandAloneConnectionManager
import fr.delphes.bot.connector.connectionstate.ConnectionSuccessful
import fr.delphes.bot.connector.connectionstate.ConnectorTransition
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.discord.incomingEvent.NewGuildMember
import fr.delphes.connector.discord.outgoingEvent.DiscordOutgoingEvent
import kotlinx.coroutines.launch

class DiscordConnectionManager(
    private val connector: DiscordConnector
) : StandAloneConnectionManager<DiscordConfiguration, DiscordRunTime>(connector.configurationManager) {
    override val connectionName = "Kord"

    override suspend fun doConnection(configuration: DiscordConfiguration): ConnectorTransition<DiscordConfiguration, DiscordRunTime> {
        val client = Kord(configuration.oAuthToken)
        client.on<MemberJoinEvent> {
            connector.bot.handle(NewGuildMember(this.member.displayName))
        }

        scope.launch {
            client.login {
                @OptIn(PrivilegedIntent::class)
                intents = Intents.nonPrivileged + Intent.GuildMembers
            }
        }
        return ConnectionSuccessful(
            configuration,
            DiscordRunTime(client)
        )
    }

    override suspend fun execute(event: OutgoingEvent) {
        if (event is DiscordOutgoingEvent) {
            event.executeOnDiscord(connector)
        }
    }
}