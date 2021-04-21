package fr.delphes.features.twitch.gameDescription

import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.command.CommandHandler
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.feature.NonEditableFeature
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.games.WithGameId

//TODO dynamics description (file / commands / ... ?)
class GameDescription(
    override val channel: TwitchChannel,
    commandTrigger: String,
    private val descriptions: Map<GameId, String>
) : NonEditableFeature<GameDescriptionDescription>, TwitchFeature {
    constructor(
        channel: TwitchChannel,
        commandTrigger: String,
        vararg descriptions: Pair<WithGameId, String>
    ) : this(channel, commandTrigger, mapOf(*descriptions).mapKeys { entry -> entry.key.gameId })

    override fun description() = GameDescriptionDescription(channel.name)

    val command = Command(commandTrigger)

    override val eventHandlers = run {
        val handlers = EventHandlers()
        handlers.addHandler(buildCommandHandler())
        handlers
    }

    private fun buildCommandHandler() = CommandHandler(channel, command) { _, twitchConnector ->  this.displayInfoFor(twitchConnector) }
    override val commands: Iterable<Command> = listOf(command)

    private suspend fun displayInfoFor(
        twitchConnector: TwitchConnector
    ): List<OutgoingEvent> {
        return twitchConnector.whenRunning(
            whenRunning = {
                val description =
                    clientBot.channelOf(channel)!!.currentStream?.game?.id?.let { game -> descriptions[game] }
                description?.let { listOf(SendMessage(it, channel)) } ?: emptyList()
            },
            whenNotRunning = {
                emptyList()
            }
        )
    }
}