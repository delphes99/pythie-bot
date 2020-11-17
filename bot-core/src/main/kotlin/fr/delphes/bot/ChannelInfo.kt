package fr.delphes.bot

import fr.delphes.bot.command.Command
import fr.delphes.bot.state.Statistics
import fr.delphes.twitch.model.Stream

interface ChannelInfo {
    val commands: List<Command>
    val currentStream: Stream?
    //TODO statistics when stream offline
    //TODO global statistics ?
    val statistics: Statistics?
}