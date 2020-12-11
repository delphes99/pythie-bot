package fr.delphes.twitch

import fr.delphes.twitch.api.user.payload.GetUsersDataPayload
import fr.delphes.twitch.eventSub.payload.subscription.ListSubscriptionsPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscriptionPayload

interface AppHelixApi {
    suspend fun getEventSubSubscriptions(): ListSubscriptionsPayload

    suspend fun removeEventSubSubscription(subscription: SubscriptionPayload)

    suspend fun getUser(userName: String): GetUsersDataPayload?
}