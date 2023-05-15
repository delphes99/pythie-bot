package fr.delphes.rework.feature

import fr.delphes.utils.uuid.uuid
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class FeatureId(val value: String = uuid()) {
    override fun toString(): String {
        return value
    }
}