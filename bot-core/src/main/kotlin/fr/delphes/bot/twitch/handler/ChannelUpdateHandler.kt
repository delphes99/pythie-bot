package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.StreamChanged
import fr.delphes.bot.event.incoming.StreamChanges
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.api.channelUpdate.ChannelUpdate
import fr.delphes.twitch.api.streams.Stream

class ChannelUpdateHandler : TwitchIncomingEventHandler<ChannelUpdate> {
    override suspend fun handle(
        twitchEvent: ChannelUpdate,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        val currentStream = channel.currentStream

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
            changeState.changeCurrentStream(currentStream.applyChanges(changes))
            listOf(StreamChanged(changes))
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