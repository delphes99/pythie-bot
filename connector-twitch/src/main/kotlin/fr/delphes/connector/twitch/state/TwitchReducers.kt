package fr.delphes.connector.twitch.state

import fr.delphes.connector.twitch.state.reducer.MessageReceivedReducer
import fr.delphes.connector.twitch.state.reducer.StreamChangeReducer
import fr.delphes.utils.store.wrap

val twitchReducers = listOf(
    MessageReceivedReducer().wrap(),
    StreamChangeReducer().wrap()
)