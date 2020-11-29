package fr.delphes.twitch.eventSub

import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.GenericCondition
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport

interface EventSubSubscribe<T: GenericCondition> {
    val condition: T
    val transport: SubscribeTransport
    val type: EventSubSubscriptionType
    val version: String
}