package fr.delphes.connector.twitch.state

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.command.Command
import fr.delphes.state.State
import fr.delphes.state.StateId
import fr.delphes.twitch.TwitchChannel

class CommandListState(
    private val connector: TwitchConnector
) : State {
    override val id = stateId

    fun getCommandsOf(channel: TwitchChannel): List<Command> = connector.commandsFor(channel)

    companion object {
        val stateId = StateId("twitch-connector-command-list")
    }
}