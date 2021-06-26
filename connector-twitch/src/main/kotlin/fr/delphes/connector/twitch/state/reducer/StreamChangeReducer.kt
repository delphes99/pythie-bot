package fr.delphes.connector.twitch.state.reducer

import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.connector.twitch.state.GameInfo
import fr.delphes.connector.twitch.state.TwitchChannelReducer
import fr.delphes.connector.twitch.state.TwitchChannelState
import fr.delphes.connector.twitch.state.action.StreamChangeAction

class StreamChangeReducer : TwitchChannelReducer<StreamChangeAction> {
    override fun apply(
        action: StreamChangeAction,
        state: TwitchChannelState
    ): TwitchChannelState {
        val newCurrentStream = state.currentStream?.let { currentStream ->
            action.changes.fold(currentStream) { acc, change ->
                when (change) {
                    is StreamChanges.Game -> acc.copy(game = GameInfo(change.newGame.id, change.newGame.label))
                    is StreamChanges.Title -> acc.copy(title = change.newTitle)
                }
            }
        }

        return state.copy(currentStream = newCurrentStream)
    }
}