package fr.delphes.twitch

import fr.delphes.twitch.api.user.payload.GetUsersDataPayload
import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.GenericCondition
import fr.delphes.twitch.eventSub.payload.subscription.ListSubscriptionsPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscriptionPayload

interface AppHelixApi {
    suspend fun getEventSubSubscriptions(): ListSubscriptionsPayload

    suspend fun subscribeEventSub(subscribe: EventSubSubscribe<out GenericCondition>)

    suspend fun removeEventSubSubscription(subscription: SubscriptionPayload)

    suspend fun getUser(userName: String): GetUsersDataPayload?
}