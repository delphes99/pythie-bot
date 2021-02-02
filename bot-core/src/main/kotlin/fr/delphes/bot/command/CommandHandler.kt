package fr.delphes.bot.command

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.twitch.api.user.User

class CommandHandler(
    val command: Command,
    private val doCommand: (User, ClientBot) -> List<OutgoingEvent>
) : EventHandler<CommandAsked> {
    override suspend fun handle(event: CommandAsked, bot: ClientBot): List<OutgoingEvent> {
        return if(event.command == command) {
            doCommand(event.by, bot)
        } else {
            emptyList()
        }
    }
}