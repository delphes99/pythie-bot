package fr.delphes.feature.gameDescription

import fr.delphes.User
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.command.Command
import fr.delphes.bot.command.CommandHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.feature.AbstractFeature
import fr.delphes.twitch.model.GameId

class GameDescription(commandTrigger: String, vararg descriptions: Pair<GameId, String>) : AbstractFeature() {
    //TODO dynamics description (file / commands / ... ?)
    private val descriptions = mapOf(*descriptions).mapKeys { entry -> entry.key.id }
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(commandHandler)
    }

    private val commandHandler = CommandHandler(Command(commandTrigger), this::displayInfoFor)
    override val commands: Iterable<Command> = listOf(commandHandler.command)

    private fun displayInfoFor(
        @Suppress("UNUSED_PARAMETER") user: User,
        channelInfo: ChannelInfo
    ): List<OutgoingEvent> {
        val description = channelInfo.currentStream?.game?.id?.id?.let { game -> descriptions[game] }
        return description?.let { listOf(SendMessage(it)) } ?: emptyList()
    }
}