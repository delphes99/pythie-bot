package fr.delphes.features.twitch.gameDescription

import fr.delphes.bot.ClientBot
import fr.delphes.bot.command.Command
import fr.delphes.bot.command.CommandHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.feature.TwitchFeature
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.games.WithGameId
import fr.delphes.twitch.api.user.User

//TODO dynamics description (file / commands / ... ?)
class GameDescription(
    channel: TwitchChannel,
    commandTrigger: String,
    private val descriptions: Map<GameId, String>
) : TwitchFeature(channel) {
    constructor(
        channel: TwitchChannel,
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
        bot: ClientBot
    ): List<OutgoingEvent> {
        val description = bot.channelOf(channel)?.currentStream?.game?.id?.let { game -> descriptions[game] }
        return description?.let { listOf(SendMessage(it)) } ?: emptyList()
    }
}