package fr.delphes.connector.twitch.outgoingEvent.descriptionMapper

import fr.delphes.generation.dynamicForm.FieldDescriptorMapper
import fr.delphes.twitch.TwitchChannel

object TwitchChannelMapper : FieldDescriptorMapper<TwitchChannel> {
    override fun mapFromDto(value: String): TwitchChannel {
        return TwitchChannel(value)
    }

    override fun mapToDto(value: TwitchChannel): String {
        return value.name
    }
}