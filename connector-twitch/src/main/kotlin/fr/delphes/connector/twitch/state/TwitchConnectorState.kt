package fr.delphes.connector.twitch.state

import fr.delphes.twitch.TwitchChannel
import kotlinx.serialization.Serializable

@Serializable
data class TwitchConnectorState(
    private val channelStates: Map<TwitchChannel, TwitchChannelState> = emptyMap()
) {
    constructor(vararg states: Pair<TwitchChannel, TwitchChannelState>) : this(mapOf(*states))

    fun stateOf(channel: TwitchChannel) : TwitchChannelState {
        return channelStates[channel] ?: TwitchChannelState()
    }

    internal fun modifyChannelState(
        channel: TwitchChannel,
        modify: (TwitchChannelState) -> TwitchChannelState
    ): TwitchConnectorState {
        val oldChannelState = stateOf(channel)
        val newChannelState = modify(oldChannelState)
        return this.copy(
            channelStates = channelStates.filter { it.key != channel }.plus(channel to newChannelState)
        )
    }
}
