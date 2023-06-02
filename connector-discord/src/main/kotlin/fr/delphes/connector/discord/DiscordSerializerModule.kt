package fr.delphes.connector.discord

import fr.delphes.connector.discord.incomingEvent.discordIncomingEventSerializerModule
import kotlinx.serialization.modules.SerializersModule

val discordSerializerModule = SerializersModule {
    include(discordIncomingEventSerializerModule)
}