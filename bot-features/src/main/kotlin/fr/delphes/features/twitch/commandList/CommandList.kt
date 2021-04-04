package fr.delphes.features.twitch.commandList

import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.NonEditableTwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.command.CommandHandler
import fr.delphes.twitch.TwitchChannel

class CommandList(
    channel: TwitchChannel,
    private val triggerMessage: String,
    displayCommands: (List<String>) -> List<OutgoingEvent>
) : NonEditableTwitchFeature<CommandListDescription>(channel) {
    override fun description() = CommandListDescription(channel.name, triggerMessage)

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(commandHandler)
    }

    private val commandHandler = CommandHandler(
        channel,
        Command(triggerMessage)
    ) { _, twitchConnector ->
        twitchConnector.whenRunning(
            whenRunning = {
                val commands = this.clientBot.commandsFor(channel).map(Command::triggerMessage)

                displayCommands(commands)
            },
            whenNotRunning = {
                emptyList()
            }
        )
    }

    override val commands: Iterable<Command> = listOf(commandHandler.command)
}