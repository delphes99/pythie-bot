package fr.delphes.bot

import fr.delphes.feature.descriptor.DurationFeatureDescriptor
import fr.delphes.feature.descriptor.FeatureDescriptor
import fr.delphes.feature.descriptor.OutgoingEventsFeatureDescriptor
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val coreSerializersModule = SerializersModule {
    polymorphic(FeatureDescriptor::class) {
        subclass(StringFeatureDescriptor::class)
        subclass(DurationFeatureDescriptor::class)
        subclass(OutgoingEventsFeatureDescriptor::class)
    }
}