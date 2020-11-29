package fr.delphes.twitch

import fr.delphes.twitch.auth.TwitchAppCredential
import fr.delphes.twitch.eventSub.payload.subscription.ListSubscriptionsPayload
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import mu.KotlinLogging

class AppTwitchClient(
    private val twitchAppHelixApi: AppHelixApi
) : AppTwitchApi {
    override suspend fun getAllSubscriptions(): ListSubscriptionsPayload {
        return coroutineScope {
            twitchAppHelixApi.getEventSubSubscriptions()
        }
    }

    override suspend fun removeAllSubscriptions() {
        return coroutineScope {
            getAllSubscriptions().data.map { subscription ->
                launch {
                    LOGGER.debug { "Remove subscription ${subscription.transport.callback}" }
                    twitchAppHelixApi.removeEventSubSubscription(subscription)
                }
            }.joinAll()
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
        fun build(appCredential: TwitchAppCredential): AppTwitchClient {
            return AppTwitchClient(AppHelixClient(appCredential))
        }
    }
}