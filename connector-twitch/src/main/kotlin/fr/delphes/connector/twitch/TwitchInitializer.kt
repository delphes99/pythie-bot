package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.incomingEvent.twitchIncomingEventSerializerModule
import fr.delphes.connector.twitch.outgoingEvent.twitchOutgoingEventSerializerModule

class TwitchInitializer(
    private val channelConfigurations: List<ChannelConfiguration>,
) : ConnectorInitializer() {
    constructor(
        vararg channels: ChannelConfiguration,
    ) : this(channels.toList())

    override val incomingEventSerializerModule = twitchIncomingEventSerializerModule
    override val outgoingEventBuilderSerializerModule = twitchOutgoingEventSerializerModule

    override fun buildConnector(bot: Bot) = TwitchConnector(
        bot,
        bot.configuration,
        channelConfigurations
    )
}