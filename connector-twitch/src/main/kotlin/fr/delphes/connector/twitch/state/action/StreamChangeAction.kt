package fr.delphes.connector.twitch.state.action

import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.connector.twitch.state.TwitchChannelAction
import fr.delphes.twitch.TwitchChannel

data class StreamChangeAction(
    override val channel: TwitchChannel,
    val changes: List<StreamChanges>
) : TwitchChannelAction