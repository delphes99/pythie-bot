package fr.delphes.bot.event.outgoing

import fr.delphes.bot.event.builder.OutgoingEventBuilder
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class PlaySound(val mediaName: String) : CoreOutgoingEvent {
    @Serializable
    @SerialName("core-play-sound")
    class Builder(
        private val mediaName: String = "",
    ) : OutgoingEventBuilder {
        override fun build() = PlaySound(mediaName)

        override fun description(): OutgoingEventBuilderDescription {
            return OutgoingEventBuilderDescription(
                type = "core-play-sound",
                descriptors = listOf(
                    StringFeatureDescriptor(
                        fieldName = "mediaName",
                        description = "Media to play",
                        value = mediaName
                    )
                )
            )
        }
    }
}