package fr.delphes.connector.twitch.state

import fr.delphes.connector.twitch.state.reducer.messageReceivedReducer
import fr.delphes.utils.store.wrap

val twitchReducers = listOf(
    messageReceivedReducer.wrap()
)