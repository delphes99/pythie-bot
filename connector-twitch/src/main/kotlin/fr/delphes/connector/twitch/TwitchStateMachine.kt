package fr.delphes.connector.twitch

class TwitchStateMachine(
    private val twitchConnector: TwitchConnector
) {
    private var state: TwitchState = TwitchState.Unconfigured

    fun on(event: TwitchStateEvent) {
        state = state.on(event)
    }

    suspend fun whenRunning(function: suspend TwitchState.AppConnected.() -> Unit) {
        state.whenRunning(function)
    }

    suspend fun <T> whenRunning(
        whenRunning: suspend TwitchState.AppConnected.() -> T,
        whenNotRunning: suspend () -> T,
    ): T {
        val currentState = state
        return if (currentState is TwitchState.AppConnected) {
            currentState.whenRunning()
        } else {
            whenNotRunning()
        }
    }
}