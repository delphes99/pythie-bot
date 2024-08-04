package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.reward.ConfiguredRewards
import fr.delphes.twitch.generated.dynamicForm.twitchDynamicFormRegistry
import fr.delphes.twitch.generated.twitchPolymorphicSerializerModule

class TwitchInitializer(
    private val channelConfigurations: List<ChannelConfiguration>,
    private val configuredRewards: ConfiguredRewards,
) : ConnectorInitializer() {
    override val serializerModule = twitchPolymorphicSerializerModule
    override val dynamicFormRegistry = twitchDynamicFormRegistry

    override fun buildConnector(bot: Bot) = TwitchConnector(
        bot,
        bot.configuration,
        channelConfigurations,
        configuredRewards
    )
}