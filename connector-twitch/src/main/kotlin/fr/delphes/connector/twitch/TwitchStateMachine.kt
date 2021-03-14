package fr.delphes.connector.twitch

class TwitchStateMachine(
    private val twitchConnector: TwitchConnector
) {
    private var state: TwitchState = TwitchState.Unconfigured

    fun on(event: TwitchStateEvent) {
        state = state.on(event)
    }
}