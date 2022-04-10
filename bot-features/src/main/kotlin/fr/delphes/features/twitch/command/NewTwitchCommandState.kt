@file:UseSerializers(NullableLocalDateTimeSerializer::class)

package fr.delphes.features.twitch.command

import fr.delphes.feature.featureNew.FeatureState
import fr.delphes.utils.serialization.NullableLocalDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
class NewTwitchCommandState(
    val lastCall: LocalDateTime? = null
) : FeatureState