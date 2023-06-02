package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.bot.event.incoming.IncomingEvent
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

internal val twitchIncomingEventSerializerModule = SerializersModule {
    polymorphic(IncomingEvent::class) {
        subclass(BitCheered::class)
        subclass(ClipCreated::class)
        subclass(NewSub::class)
        subclass(NewPoll::class)
        subclass(IncomingRaid::class)
        subclass(MessageReceived::class)
        subclass(CommandAsked::class)
        subclass(NewFollow::class)
        subclass(PollUpdated::class)
        subclass(PollClosed::class)
        subclass(RewardRedemption::class)
        subclass(StreamChanged::class)
        subclass(StreamOffline::class)
        subclass(StreamOnline::class)
    }
}