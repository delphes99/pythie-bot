package fr.delphes.connector.twitch.state

import fr.delphes.connector.twitch.state.reducer.MessageReceivedReducer

val twitchReducers = listOf(
    MessageReceivedReducer()
)