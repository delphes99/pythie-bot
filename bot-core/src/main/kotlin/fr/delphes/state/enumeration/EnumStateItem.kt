package fr.delphes.state.enumeration

import kotlinx.serialization.Serializable

@Serializable
data class EnumStateItem(
    val value: String,
    val label: String,
    val description: String,
)