package fr.delphes.twitch

import fr.delphes.twitch.eventSub.payload.subscription.ListSubscriptionsPayload

interface AppTwitchApi {
    suspend fun getAllSubscriptions(): ListSubscriptionsPayload

    suspend fun removeAllSubscriptions()
}