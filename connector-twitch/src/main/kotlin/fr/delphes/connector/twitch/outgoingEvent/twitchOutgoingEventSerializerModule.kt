package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.bot.event.outgoing.LegacyOutgoingEventBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val twitchOutgoingEventSerializerModule = SerializersModule {
    polymorphic(LegacyOutgoingEventBuilder::class) {
        subclass(SendMessage.Companion.Builder::class)
        subclass(ActivateReward.Companion.Builder::class)
        subclass(DeactivateReward.Companion.Builder::class)
    }
}