package fr.delphes.bot.overlay

import kotlinx.serialization.Serializable

@Serializable
data class Overlay(
    val id: String,
    val title: String,
    val resolution: Resolution,
    val elements: List<OverlayElement<*>> = emptyList(),
) {
    fun sanitize(): Overlay {
        return this.copy(
            elements = elements.reorder()
        )
    }

    private fun List<OverlayElement<*>>.reorder() = this
        .sortedBy { it.general.sortOrder }
        .mapIndexed { index, element -> element.copy(general = element.general.copy(sortOrder = index)) }
}
