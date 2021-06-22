package fr.delphes.connector.twitch.state.reducer

import fr.delphes.bot.state.UserMessage
import fr.delphes.connector.twitch.state.TwitchConnectorState
import fr.delphes.connector.twitch.state.action.MessageReceivedAction
import fr.delphes.utils.store.Reducer

val messageReceivedReducer = Reducer { action: MessageReceivedAction, state: TwitchConnectorState ->
    state.modifyChannelState(action.channel) { oldState ->
        oldState.copy(
            userMessages = oldState.userMessages.plus(UserMessage(action.user, action.text))
        )
    }
}