package fr.delphes.features

import fr.delphes.features.generated.featuresPolymorphicSerializerModule
import kotlinx.serialization.modules.SerializersModule

val featureSerializersModule = SerializersModule {
    include(featuresPolymorphicSerializerModule)
}