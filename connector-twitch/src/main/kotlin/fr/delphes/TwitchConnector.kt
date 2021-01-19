package fr.delphes

import fr.delphes.bot.Channel
import fr.delphes.bot.ClientBot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.configuration.ChannelConfiguration
import io.ktor.application.Application

class TwitchConnector(
    private val channels: List<ChannelConfiguration>
) : Connector {
    constructor(vararg channels: ChannelConfiguration) : this(listOf(*channels))

    override fun internalEndpoints(application: Application) {
    }

    override fun publicEndpoints(application: Application) {
    }

    override fun connect(bot: ClientBot) {
        channels.forEach { channelConfiguration ->
            bot.register(Channel(channelConfiguration, bot))
        }
    }

    override suspend fun execute(event: OutgoingEvent) {

    }
}