package fr.delphes.twitch

import fr.delphes.twitch.api.user.payload.GetUsersDataPayload
import fr.delphes.twitch.api.user.payload.GetUsersPayload
import fr.delphes.twitch.api.video.payload.ChannelVideoPayload
import fr.delphes.twitch.api.video.payload.ChannelVideosPayload
import fr.delphes.twitch.auth.CredentialsManager
import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.GenericCondition
import fr.delphes.twitch.eventSub.payload.subscription.ListSubscriptionsPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscriptionPayload
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse

class AppHelixClient(
    clientId: String,
    credentialsManager: CredentialsManager,
) : ScopedHelixClient(
    clientId = clientId,
    getToken = { credentialsManager.getAppToken() },
    getTokenRefreshed = { credentialsManager.getAppTokenRefreshed() }
), AppHelixApi {
    override suspend fun getEventSubSubscriptions(): ListSubscriptionsPayload {
        return "https://api.twitch.tv/helix/eventsub/subscriptions".get()
    }

    override suspend fun subscribeEventSub(subscribe: EventSubSubscribe<out GenericCondition>) {
        //TODO manage errors
        "https://api.twitch.tv/helix/eventsub/subscriptions".post<HttpResponse>(subscribe)
    }

    override suspend fun removeEventSubSubscription(subscription: SubscriptionPayload) {
        "https://api.twitch.tv/helix/eventsub/subscriptions".delete<HttpResponse>(
            "id" to subscription.id
        )
    }

    override suspend fun getUser(userName: String): GetUsersDataPayload? {
        val payload = "https://api.twitch.tv/helix/users".get<GetUsersPayload>(
            "login" to userName
        )

        return payload.data.firstOrNull()
    }

    override suspend fun getVideosOf(userId: String, limit: Int): List<ChannelVideoPayload> {
        val payload = authorizeCall { token ->
            httpClient.get<ChannelVideosPayload>("https://api.twitch.tv/kraken/channels/$userId/videos?limit=$limit") {
                this.header("Authorization", "Bearer ${token.access_token}")
                this.header("Client-Id", clientId)
                this.header("Accept", "application/vnd.twitchtv.v5+json")
            }
        }

        return payload.videos
    }
}