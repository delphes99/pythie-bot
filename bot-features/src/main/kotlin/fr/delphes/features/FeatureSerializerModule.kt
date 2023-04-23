package fr.delphes.features

import fr.delphes.feature.FeatureConfiguration
import fr.delphes.features.twitch.command.CustomCommandConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val featureSerializersModule = SerializersModule {
    polymorphic(FeatureConfiguration::class) {
        subclass(CustomCommandConfiguration::class)
    }
}