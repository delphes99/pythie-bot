package fr.delphes.feature

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class OutgoingEventType(val name: String)