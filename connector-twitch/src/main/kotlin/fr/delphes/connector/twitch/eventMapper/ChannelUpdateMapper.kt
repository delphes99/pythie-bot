package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.StreamChanged
import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.api.channelUpdate.ChannelUpdate
import fr.delphes.twitch.api.streams.Stream

class ChannelUpdateMapper(
    private val connector: TwitchConnector
) : TwitchIncomingEventMapper<ChannelUpdate> {
    override suspend fun handle(
        twitchEvent: ChannelUpdate
    ): List<TwitchIncomingEvent> {
        return connector.whenRunning(
            whenRunning = {
                val currentStream = clientBot.channelOf(twitchEvent.channel)?.currentStream

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
                if (!changes.isNullOrEmpty()) {
                    //TODO move to connector implementation
                    clientBot.channelOf(twitchEvent.channel)?.state?.changeCurrentStream(currentStream.applyChanges(changes))
                    listOf(StreamChanged(twitchEvent.channel, changes))
                } else {
                    emptyList()
                }
            },
            whenNotRunning = {
                emptyList()
            }
        )
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