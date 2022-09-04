@file:UseSerializers(DurationSerializer::class)

package fr.delphes.descriptor.item

import fr.delphes.utils.serialization.DurationSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.UseSerializers
import java.time.Duration

@kotlinx.serialization.Serializable
@SerialName("description-duration")
data class DurationDescriptor(
    val name: String,
    val value: Duration? = null
) : ItemDescriptor()