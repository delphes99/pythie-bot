package fr.delphes.features.twitch.commandList

import fr.delphes.bot.command.Command
import fr.delphes.bot.command.CommandHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.TwitchFeature

class CommandList(
    channel: String,
    triggerMessage: String,
    displayCommands: (List<String>) -> List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(commandHandler)
    }

    private val commandHandler = CommandHandler(
        Command(triggerMessage)
    ) { _, channel ->
        val commands = channel.commands.map(Command::triggerMessage)

        displayCommands(commands)
    }

    override val commands: Iterable<Command> = listOf(commandHandler.command)
}