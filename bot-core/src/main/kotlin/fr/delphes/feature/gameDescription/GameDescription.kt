package fr.delphes.feature.gameDescription

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.command.Command
import fr.delphes.bot.command.CommandHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.feature.AbstractFeature
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.games.WithGameId
import fr.delphes.twitch.api.user.User


//TODO dynamics description (file / commands / ... ?)
class GameDescription(
    commandTrigger: String,
    private val descriptions: Map<GameId, String>
) : AbstractFeature() {
    constructor(
        commandTrigger: String,
        vararg descriptions: Pair<WithGameId, String>
    ): this(commandTrigger, mapOf(*descriptions).mapKeys { entry -> entry.key.gameId })

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(commandHandler)
    }

    private val commandHandler = CommandHandler(Command(commandTrigger), this::displayInfoFor)
    override val commands: Iterable<Command> = listOf(commandHandler.command)

    private fun displayInfoFor(
        @Suppress("UNUSED_PARAMETER") user: User,
        channelInfo: ChannelInfo
    ): List<OutgoingEvent> {
        val description = channelInfo.currentStream?.game?.id?.let { game -> descriptions[game] }
        return description?.let { listOf(SendMessage(it)) } ?: emptyList()
    }
}