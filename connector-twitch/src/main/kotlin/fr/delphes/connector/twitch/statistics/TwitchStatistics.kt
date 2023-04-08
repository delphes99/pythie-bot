package fr.delphes.connector.twitch.statistics

import fr.delphes.bot.configuration.BotConfiguration
import fr.delphes.bot.state.ChannelStatistics
import fr.delphes.bot.state.FileStatisticsRepository
import fr.delphes.twitch.TwitchChannel

class TwitchStatistics(
    private val botConfiguration: BotConfiguration,
) {
    private val channels = mutableMapOf<TwitchChannel, ChannelStatistics>()

    fun of(channel: TwitchChannel): ChannelStatistics {
        return channels.getOrPut(channel) {
            ChannelStatistics(FileStatisticsRepository(botConfiguration.pathOf(channel.normalizeName)))
        }
    }
}