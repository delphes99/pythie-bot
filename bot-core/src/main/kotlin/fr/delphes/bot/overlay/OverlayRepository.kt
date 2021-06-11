package fr.delphes.bot.overlay

import fr.delphes.utils.FileRepository
import fr.delphes.utils.serialization.Serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class OverlayRepository(
    path: String
) : FileRepository<List<Overlay>>(
    path,
    serializer = { Serializer.encodeToString(it) },
    deserializer = { Serializer.decodeFromString(it) },
    initializer = { emptyList() }
) {
    suspend fun upsert(overlay: Overlay) {
        save(
            load()
                .filterNot { it.id == overlay.id }
                .plus(overlay)
        )
    }
}