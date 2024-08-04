package fr.delphes.overlay.state

import fr.delphes.bot.media.Media
import fr.delphes.bot.media.MediatType
import fr.delphes.overlay.OverlayConnector
import fr.delphes.state.StateId
import fr.delphes.state.enumeration.EnumStateItem
import fr.delphes.state.enumeration.EnumerationState

class SoundListState(
    val connector: OverlayConnector,
) : EnumerationState<Media> {
    override val id = ID

    override suspend fun getItems() = connector.bot.mediaService.list()
        .filter(MediatType.SOUND::hasType)
        .map(Media::filename)
        .map { filename ->
            EnumStateItem(
                filename.replace(".mp3", ""),
                filename,
                filename
            )
        }

    override fun deserialize(serializeValue: String): Media {
        return Media(serializeValue)
    }

    companion object {
        val ID = StateId.from<SoundListState>("sound-list")
    }
}