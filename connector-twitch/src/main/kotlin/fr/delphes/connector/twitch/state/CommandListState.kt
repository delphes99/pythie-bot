package fr.delphes.connector.twitch.state

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.command.Command
import fr.delphes.state.State
import fr.delphes.state.StateIdQualifier
import fr.delphes.state.StateId
import fr.delphes.twitch.TwitchChannel

class CommandListState(
    private val connector: TwitchConnector
) : State {
    override val id = ID

    fun getCommandsOf(channel: TwitchChannel): List<Command> = connector.commandsFor(channel)

    companion object {
        val stateIdQualifier = StateIdQualifier("twitch-connector-command-list")
        val ID = StateId.from<CommandListState>(stateIdQualifier)
    }
}