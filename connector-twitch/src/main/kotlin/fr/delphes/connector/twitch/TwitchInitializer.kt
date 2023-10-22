package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.twitch.generated.twitchOutgoingEventRegistry
import fr.delphes.twitch.generated.twitchPolymorphicSerializerModule

class TwitchInitializer(
    private val channelConfigurations: List<ChannelConfiguration>,
) : ConnectorInitializer() {
    constructor(
        vararg channels: ChannelConfiguration,
    ) : this(channels.toList())

    override val serializerModule = twitchPolymorphicSerializerModule
    override val outgoingEventRegistry = twitchOutgoingEventRegistry

    override fun buildConnector(bot: Bot) = TwitchConnector(
        bot,
        bot.configuration,
        channelConfigurations
    )
}