package fr.delphes.features.twitch.commandList

import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.command.LegacyCommandHandler
import fr.delphes.feature.NonEditableFeature
import fr.delphes.twitch.TwitchChannel

class CommandList(
    override val channel: TwitchChannel,
    private val triggerMessage: String,
    private val displayCommands: (List<String>) -> List<OutgoingEvent>
) : NonEditableFeature, TwitchFeature {
    private val command = Command(triggerMessage)

    override val eventHandlers =LegacyEventHandlers
        .builder()
        .addHandler(buildCommandHandler())
        .build()

    private fun buildCommandHandler() = LegacyCommandHandler(
        channel,
        command
    ) { _, twitchConnector ->
        val commands = twitchConnector.commandsFor(this@CommandList.channel).map(Command::triggerMessage)

        displayCommands(commands)
    }

    override val commands: Iterable<Command> = listOf(command)
}