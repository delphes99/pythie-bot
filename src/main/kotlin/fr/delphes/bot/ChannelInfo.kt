package fr.delphes.bot

import fr.delphes.bot.command.Command

interface ChannelInfo {
    val commands: List<Command>
}