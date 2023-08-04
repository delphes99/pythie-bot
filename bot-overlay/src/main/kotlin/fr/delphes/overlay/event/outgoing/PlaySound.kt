package fr.delphes.overlay.event.outgoing

import fr.delphes.bot.event.outgoing.LegacyOutgoingEventBuilder
import fr.delphes.bot.event.outgoing.WithBuilder
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.OutgoingEventType
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import fr.delphes.state.StateManager
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class PlaySound(val mediaName: String) : OverlayOutgoingEvent {
    companion object : WithBuilder {
        override val type = OutgoingEventType("core-play-sound")

        override val builderDefinition = buildDefinition(Companion::Builder)

        @Serializable
        @SerialName("core-play-sound")
        class Builder(
            private val mediaName: String = "",
        ) : LegacyOutgoingEventBuilder {
            override suspend fun build(stateManager: StateManager) = PlaySound(mediaName)

            override fun description(): OutgoingEventBuilderDescription {
                return buildDescription(
                    StringFeatureDescriptor(
                        fieldName = "mediaName",
                        description = "Media to play",
                        value = mediaName
                    )
                )
            }
        }
    }
}