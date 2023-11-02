package fr.delphes.connector.twitch.outgoingEvent.descriptionMapper

import fr.delphes.annotation.outgoingEvent.createBuilder.FieldDescriptorMapper
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel

object TwitchChannelMapper : FieldDescriptorMapper<TwitchChannel> {
    override suspend fun map(value: String, stateProvider: StateProvider): TwitchChannel {
        return TwitchChannel(value)
    }
}