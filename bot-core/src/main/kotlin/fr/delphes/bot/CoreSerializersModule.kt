package fr.delphes.bot

import fr.delphes.bot.event.incoming.incomingEventSerializerModule
import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.bot.event.outgoing.PlaySound
import fr.delphes.feature.descriptor.DurationFeatureDescriptor
import fr.delphes.feature.descriptor.FeatureDescriptor
import fr.delphes.feature.descriptor.OutgoingEventsFeatureDescriptor
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import statisticSerializerModule

val coreSerializersModule = SerializersModule {
    polymorphic(FeatureDescriptor::class) {
        subclass(StringFeatureDescriptor::class)
        subclass(DurationFeatureDescriptor::class)
        subclass(OutgoingEventsFeatureDescriptor::class)
    }
    polymorphic(OutgoingEventBuilder::class) {
        subclass(PlaySound.Companion.Builder::class)
    }
    include(statisticSerializerModule)
    include(incomingEventSerializerModule)
}