package fr.delphes.connector.twitch.state.reducer

import fr.delphes.bot.state.UserMessage
import fr.delphes.connector.twitch.state.TwitchChannelReducer
import fr.delphes.connector.twitch.state.TwitchChannelState
import fr.delphes.connector.twitch.state.action.MessageReceivedAction

class MessageReceivedReducer : TwitchChannelReducer<MessageReceivedAction> {
    override fun apply(action: MessageReceivedAction, state: TwitchChannelState): TwitchChannelState {
        return state.copy(
            userMessages = state.userMessages.plus(UserMessage(action.user, action.text))
        )
    }
}