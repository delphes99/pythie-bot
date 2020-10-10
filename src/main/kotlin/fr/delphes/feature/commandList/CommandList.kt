package fr.delphes.feature.commandList

import fr.delphes.bot.command.Command
import fr.delphes.bot.command.CommandHandler
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.AbstractFeature

class CommandList(
    triggerMessage: String,
    displayCommands: (List<String>) -> List<OutgoingEvent>
) : AbstractFeature() {
    private val command = CommandHandler(
        triggerMessage
    ) { _, channel ->
        val commands = channel.commands.map(Command::triggerMessage)

        displayCommands(commands)
    }

    override val commands: Iterable<Command> = listOf(command)
}