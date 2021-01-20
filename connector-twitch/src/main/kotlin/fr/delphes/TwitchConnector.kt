package fr.delphes

import fr.delphes.bot.Channel
import fr.delphes.bot.ClientBot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.webservice.AuthModule
import io.ktor.application.Application

class TwitchConnector(
    private val channels: List<ChannelConfiguration>
) : Connector {
    constructor(vararg channels: ChannelConfiguration) : this(listOf(*channels))

    override fun initChannel(bot: ClientBot) {
        channels.forEach { channelConfiguration ->
            bot.register(Channel(channelConfiguration, bot))
        }
    }

    override fun internalEndpoints(application: Application, bot: ClientBot) {
    }

    override fun publicEndpoints(application: Application, bot: ClientBot) {
        application.AuthModule(channels)
    }

    override fun connect(bot: ClientBot) {
        bot.connect()
    }

    override suspend fun execute(event: OutgoingEvent) {

    }
}