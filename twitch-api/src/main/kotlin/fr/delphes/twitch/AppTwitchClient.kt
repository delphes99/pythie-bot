package fr.delphes.twitch

import fr.delphes.twitch.auth.TwitchAppCredential
import fr.delphes.twitch.eventSub.payload.ListSubscriptionsPayload
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class AppTwitchClient(
    private val twitchAppHelixApi: AppHelixApi
) : AppTwitchApi {
    override suspend fun removeAllWebhooks() {
        coroutineScope {
            val webhook = twitchAppHelixApi.getWebhook()

            webhook.map {
                launch {
                    twitchAppHelixApi.unsubscribeWebhook(it)
                }
            }.joinAll()
        }
    }

    override suspend fun getAllSubscriptions(): ListSubscriptionsPayload {
        return coroutineScope {
            twitchAppHelixApi.getEventSubSubscriptions()
        }
    }

    companion object {
        fun build(appCredential: TwitchAppCredential, webhookSecret: String): AppTwitchClient {
            return AppTwitchClient(AppHelixClient(appCredential, webhookSecret))
        }
    }
}