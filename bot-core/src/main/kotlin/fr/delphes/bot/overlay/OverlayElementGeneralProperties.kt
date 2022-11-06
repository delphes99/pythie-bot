package fr.delphes.bot.overlay

import kotlinx.serialization.Serializable

@Serializable
data class OverlayElementGeneralProperties(
    val id: String,
    val left: Int,
    val top: Int,
    val sortOrder: Int = 0,
) : Comparable<OverlayElementGeneralProperties> {
    override fun compareTo(other: OverlayElementGeneralProperties) = comparator.compare(this, other)

    companion object {
        private val comparator = Comparator.comparing(OverlayElementGeneralProperties::sortOrder)
    }
}
