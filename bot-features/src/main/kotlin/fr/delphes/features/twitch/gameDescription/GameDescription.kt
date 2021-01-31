package fr.delphes.features.twitch.gameDescription

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.command.Command
import fr.delphes.bot.command.CommandHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.feature.TwitchFeature
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.games.WithGameId
import fr.delphes.twitch.api.user.User


//TODO dynamics description (file / commands / ... ?)
class GameDescription(
    channel: String,
    commandTrigger: String,
    private val descriptions: Map<GameId, String>
) : TwitchFeature(channel) {
    constructor(
        channel: String,
        commandTrigger: String,
        vararg descriptions: Pair<WithGameId, String>
    ): this(channel, commandTrigger, mapOf(*descriptions).mapKeys { entry -> entry.key.gameId })

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