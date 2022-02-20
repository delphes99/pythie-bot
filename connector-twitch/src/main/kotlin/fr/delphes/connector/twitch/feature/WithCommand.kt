package fr.delphes.connector.twitch.feature

import fr.delphes.connector.twitch.command.Command

interface WithCommand {
    val commands: Set<Command>
}