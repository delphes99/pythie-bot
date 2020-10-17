package fr.delphes.bot

import fr.delphes.bot.command.Command
import fr.delphes.bot.state.CurrentStream

interface ChannelInfo {
    val commands: List<Command>
    val currentStream: CurrentStream?
}