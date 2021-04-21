package fr.delphes.features.twitch.commandList

import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.command.CommandHandler
import fr.delphes.feature.NonEditableFeature
import fr.delphes.twitch.TwitchChannel

class CommandList(
    override val channel: TwitchChannel,
    private val triggerMessage: String,
    private val displayCommands: (List<String>) -> List<OutgoingEvent>
) : NonEditableFeature<CommandListDescription>, TwitchFeature {
    override fun description() = CommandListDescription(channel.name, triggerMessage)

    private val command = Command(triggerMessage)

    override val eventHandlers = run {
        val handlers = EventHandlers()
        handlers.addHandler(buildCommandHandler())
        handlers
    }

    private fun buildCommandHandler() = CommandHandler(
        channel,
        command
    ) { _, twitchConnector ->
        twitchConnector.whenRunning(
            whenRunning = {
                val commands = clientBot.commandsFor(this@CommandList.channel).map(Command::triggerMessage)

                displayCommands(commands)
            },
            whenNotRunning = {
                emptyList()
            }
        )
    }

    override val commands: Iterable<Command> = listOf(command)
}