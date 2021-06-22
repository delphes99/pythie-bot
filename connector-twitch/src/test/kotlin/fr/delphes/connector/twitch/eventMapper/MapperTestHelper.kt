package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.eventSub.payload.GenericCondition
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.utils.loadResourceAsText
import fr.delphes.utils.serialization.Serializer
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlin.reflect.KClass

inline fun <INCOMING: TwitchIncomingEvent, reified PAYLOAD, reified CONDITION: GenericCondition> String.shouldBeMappedTo(
    mapper: TwitchIncomingEventMapper<PAYLOAD>,
    @Suppress("unused")
    kCondition: KClass<CONDITION>,
    expected: INCOMING
) {
    val payload = loadResourceAsText(this) ?: error("no file found")
    val eventPayload = Serializer.decodeFromString<NotificationPayload<PAYLOAD, CONDITION>>(payload).event

    runBlocking {
        val incomingEvent = mapper.handle(eventPayload!!)
        incomingEvent.first() shouldBe expected
    }
}