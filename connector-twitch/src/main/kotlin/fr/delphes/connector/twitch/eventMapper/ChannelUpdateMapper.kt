package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.StreamChanged
import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelUpdate.payload.ChannelUpdateEventPayload
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.streams.Stream

class ChannelUpdateMapper(
    private val connector: TwitchConnector
) : TwitchIncomingEventMapper<ChannelUpdateEventPayload> {
    override suspend fun handle(
        twitchEvent: ChannelUpdateEventPayload
    ): List<TwitchIncomingEvent> {
        val channel = TwitchChannel(twitchEvent.broadcaster_user_name)
        val statistics = connector.statistics.of(channel)

        val currentStream = statistics.currentStream

        val changes = currentStream?.let {
            listOfNotNull(
                if (currentStream.title != twitchEvent.title) {
                    StreamChanges.Title(currentStream.title, twitchEvent.title)
                } else {
                    null
                },
                if (currentStream.game.id.id != twitchEvent.category_id) {
                    val newCategory = Game(
                        GameId(twitchEvent.category_id),
                        twitchEvent.category_name
                    )
                    StreamChanges.Game(currentStream.game, newCategory)
                } else {
                    null
                }
            )
        }

        return if (!changes.isNullOrEmpty()) {
            statistics.changeCurrentStream(currentStream.applyChanges(changes))

            listOf(StreamChanged(channel, changes))
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