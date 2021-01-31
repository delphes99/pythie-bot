package fr.delphes.connector.discord.api

import fr.delphes.connector.discord.DiscordConnector
import fr.delphes.connector.discord.incomingEvent.NewGuildMember
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class JoinListener(
    private val connector: DiscordConnector
) : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        //TODO non blocking
        runBlocking {
            connector.clientBot!!.handleEvent(NewGuildMember(event.user.name))
        }
    }
}