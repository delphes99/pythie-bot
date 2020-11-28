package fr.delphes.twitch

import io.ktor.routing.Routing

interface WebhookApi {
    fun startWebhooks(routing: Routing)

    suspend fun registerWebhooks()
}