package fr.delphes.bot

import fr.delphes.bot.command.Command
import fr.delphes.bot.state.Statistics
import fr.delphes.bot.state.StreamStatistics
import fr.delphes.twitch.api.streams.Stream

interface ChannelInfo {
    val commands: List<Command>
    val currentStream: Stream?
    val statistics: Statistics?
    val streamStatistics: StreamStatistics?
}