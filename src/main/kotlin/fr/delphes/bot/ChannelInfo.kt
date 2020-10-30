package fr.delphes.bot

import fr.delphes.bot.command.Command
import fr.delphes.bot.state.CurrentStream
import fr.delphes.bot.state.Statistics

interface ChannelInfo {
    val commands: List<Command>
    val currentStream: CurrentStream?
    //TODO statistics when stream offline
    //TODO global statistics ?
    val statistics: Statistics?
}