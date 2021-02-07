package fr.delphes.connector.twitch.eventHandler

import fr.delphes.connector.twitch.ClientBot
import fr.delphes.connector.twitch.incomingEvent.StreamChanged
import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.api.channelUpdate.ChannelUpdate
import fr.delphes.twitch.api.streams.Stream

class ChannelUpdateHandler(
    private val bot: ClientBot
) : TwitchIncomingEventHandler<ChannelUpdate> {
    override suspend fun handle(
        twitchEvent: ChannelUpdate
    ): List<TwitchIncomingEvent> {
        val currentStream = bot.channelOf(twitchEvent.channel)?.currentStream

        val changes = currentStream?.let {
            listOfNotNull(
                if (currentStream.title != twitchEvent.title) {
                    StreamChanges.Title(currentStream.title, twitchEvent.title)
                } else {
                    null
                },
                if (currentStream.game.id.id != twitchEvent.game.id.id) {
                    StreamChanges.Game(currentStream.game, twitchEvent.game)
                } else {
                    null
                }
            )
        }
        return if (!changes.isNullOrEmpty()) {
            bot.channelOf(twitchEvent.channel)?.state?.changeCurrentStream(currentStream.applyChanges(changes))
            listOf(StreamChanged(twitchEvent.channel, changes))
        } else {
            emptyList()
        }
    }

    private fun Stream.applyChanges(
        changes: List<StreamChanges>
    ): Stream {
        return changes.fold(this) { acc, change ->
            when (change) {
                is StreamChanges.Title -> acc.copy(title = change.newTitle)
                is StreamChanges.Game -> acc.copy(game = change.newGame)
            }
        }
    }
}