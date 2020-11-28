package fr.delphes.twitch

import fr.delphes.twitch.eventSub.payload.ListSubscriptionsPayload

interface AppTwitchApi {
    suspend fun removeAllWebhooks()

    suspend fun getAllSubscriptions(): ListSubscriptionsPayload
}