package fr.delphes.feature.descriptor

import fr.delphes.annotation.RegisterFieldDescriptor
import fr.delphes.utils.serialization.DurationSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Duration

@SerialName("DURATION")
@Serializable
@RegisterFieldDescriptor
data class DurationFeatureDescriptor(
    override val fieldName: String,
    override val description: String,
    @Serializable(with = DurationSerializer::class)
    override val value: Duration?,
) : FeatureDescriptor()