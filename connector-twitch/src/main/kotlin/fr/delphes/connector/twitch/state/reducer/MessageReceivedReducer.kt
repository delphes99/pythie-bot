package fr.delphes.connector.twitch.state.reducer

import fr.delphes.bot.state.UserMessage
import fr.delphes.connector.twitch.state.TwitchConnectorState
import fr.delphes.connector.twitch.state.action.MessageReceivedAction
import fr.delphes.utils.store.Reducer

class MessageReceivedReducer :
    Reducer<TwitchConnectorState, MessageReceivedAction>(
        ::stateMutation,
        MessageReceivedAction::class.java
    ) {
        companion object {
            fun stateMutation(state: TwitchConnectorState, action: MessageReceivedAction): TwitchConnectorState {
                return state.copy(
                    userMessages = state.userMessages.plus(UserMessage(action.user, action.text))
                )
            }
        }
}