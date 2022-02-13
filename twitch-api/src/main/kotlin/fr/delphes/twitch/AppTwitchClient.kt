package fr.delphes.twitch

import fr.delphes.twitch.api.user.TwitchUser
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.api.video.ChannelVideo
import fr.delphes.twitch.api.video.payload.ChannelVideoType
import fr.delphes.twitch.auth.CredentialsManager
import fr.delphes.twitch.eventSub.payload.subscription.ListSubscriptionsPayload
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import mu.KotlinLogging

class AppTwitchClient(
    private val twitchAppHelixApi: AppHelixApi,
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

    override suspend fun getUserByName(user: User): TwitchUser? {
        return coroutineScope {
            twitchAppHelixApi.getUser(user.name)?.let { user ->
                TwitchUser(
                    user.id,
                    user.login,
                    user.broadcaster_type,
                    user.view_count
                )
            }
        }
    }

    override suspend fun getVideosOf(channelId: String, channelVideoType: ChannelVideoType): List<ChannelVideo> {
        return coroutineScope {
            twitchAppHelixApi
                .getVideosOf(channelId, channelVideoType).data
                .map { payload ->
                    ChannelVideo(
                        title = payload.title,
                        url = payload.url,
                        createdAt = payload.created_at,
                        description = payload.description
                    )
                }
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
        fun build(
            clientId: String,
            credentialsManager: CredentialsManager,
        ): AppTwitchApi {
            return AppTwitchClient(AppHelixClient(clientId, credentialsManager))
        }
    }
}