package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.StreamChanged
import fr.delphes.bot.event.incoming.StreamChanges
import fr.delphes.bot.event.incoming.StreamOffline
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.bot.twitch.game.GameRepository
import fr.delphes.bot.webserver.payload.streamInfos.StreamInfosPayload
import fr.delphes.twitch.model.SimpleGameId
import fr.delphes.twitch.model.Stream
import mu.KotlinLogging

class StreamInfosHandler(
    private val gameRepository: GameRepository
) : TwitchIncomingEventHandler<StreamInfosPayload> {
    override fun handle(
        twitchEvent: StreamInfosPayload,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        val streamInfos = twitchEvent.data
        val currentStream = channel.currentStream

        val events = if (streamInfos.isEmpty()) {
            listOf(StreamOffline)
        } else {
            streamInfos.mapNotNull { newInfos ->
                if (currentStream == null) {
                    StreamOnline(newInfos.title, newInfos.started_at, gameRepository.get(SimpleGameId(newInfos.game_id)))
                } else {
                    val changes = listOfNotNull(
                        if (currentStream.title != newInfos.title) {
                            StreamChanges.Title(currentStream.title, newInfos.title)
                        } else {
                            null
                        },
                        if (currentStream.game.id.id != newInfos.game_id) {
                            StreamChanges.Game(currentStream.game, gameRepository.get(SimpleGameId(newInfos.game_id)))
                        } else {
                            null
                        }
                    )

                    if (changes.isNotEmpty()) {
                        StreamChanged(changes)
                    } else {
                        null
                    }
                }
            }
        }

        events.forEach { event ->
            when (event) {
                is StreamOnline -> {
                    changeState.changeCurrentStream(Stream(event.title, event.start, event.game))
                }
                is StreamOffline -> {
                    changeState.changeCurrentStream(null)
                }
                is StreamChanged -> {
                    changeState.changeCurrentStream(currentStream?.applyChanges(event.changes))
                }
                else -> {
                    LOGGER.error { "Event " }
                }
            }
        }

        return events
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

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}