package fr.delphes.connector.twitch.outgoingEvent.descriptionMapper

import fr.delphes.annotation.outgoingEvent.createBuilder.FieldDescriptorMapper
import fr.delphes.twitch.TwitchChannel

object TwitchChannelMapper : FieldDescriptorMapper<TwitchChannel> {
    override fun map(value: String): TwitchChannel {
        return TwitchChannel(value)
    }
}