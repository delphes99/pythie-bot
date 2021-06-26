package fr.delphes.connector.twitch.state

import fr.delphes.utils.store.Reducer

fun interface TwitchChannelReducer<ACTION : TwitchChannelAction> : Reducer<TwitchConnectorState, ACTION> {
    fun apply(action: ACTION, state: TwitchChannelState): TwitchChannelState

    override fun apply(action: ACTION, state: TwitchConnectorState): TwitchConnectorState {
        return state.modifyChannelState(action.channel) { oldState ->
            apply(action, oldState)
        }
    }
}