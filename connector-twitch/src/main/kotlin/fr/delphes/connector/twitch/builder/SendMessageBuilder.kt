package fr.delphes.connector.twitch.builder

import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.bot.event.outgoing.OutgoingEventBuilderDescription
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.feature.featureNew.FeatureDescriptionItem
import fr.delphes.feature.featureNew.FeatureDescriptionItemType
import fr.delphes.twitch.TwitchChannel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNull

private const val typeId = "twitch-outgoing-send-message"

@Serializable
@SerialName(typeId)
data class SendMessageBuilder(
    val text: String,
    val channel: String,
) : OutgoingEventBuilder {
    override fun build() = SendMessage(text, TwitchChannel(channel))

    companion object {
        const val type = typeId

        fun description(): OutgoingEventBuilderDescription {
            return OutgoingEventBuilderDescription(
                typeId,
                listOf(
                    FeatureDescriptionItem(FeatureDescriptionItemType.STRING, "text", JsonNull),
                    FeatureDescriptionItem(FeatureDescriptionItemType.STRING, "channel", JsonNull),
                )
            )
        }
    }
}