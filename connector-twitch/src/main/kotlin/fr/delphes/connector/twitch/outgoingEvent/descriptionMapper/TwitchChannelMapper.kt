package fr.delphes.connector.twitch.outgoingEvent.descriptionMapper

import fr.delphes.dynamicForm.FieldDescriptorMapper
import fr.delphes.twitch.TwitchChannel

object TwitchChannelMapper : FieldDescriptorMapper<TwitchChannel> {
    override suspend fun map(value: String): TwitchChannel {
        return TwitchChannel(value)
    }
}