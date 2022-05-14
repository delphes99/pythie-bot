package fr.delphes.twitch

import fr.delphes.twitch.api.channel.payload.ChannelInformation
import fr.delphes.twitch.api.channel.payload.ChannelInformationPayload
import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.api.user.payload.GetUsersDataPayload
import fr.delphes.twitch.api.user.payload.GetUsersPayload
import fr.delphes.twitch.api.video.payload.ChannelVideoType
import fr.delphes.twitch.api.video.payload.ChannelVideosPayload
import fr.delphes.twitch.auth.CredentialsManager
import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.GenericCondition
import fr.delphes.twitch.eventSub.payload.subscription.ListSubscriptionsPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscriptionPayload
import io.ktor.client.call.body
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
        "https://api.twitch.tv/helix/eventsub/subscriptions".post<HttpResponse>(subscribe)
    }

    override suspend fun removeEventSubSubscription(subscription: SubscriptionPayload) {
        "https://api.twitch.tv/helix/eventsub/subscriptions".delete<HttpResponse>(
            "id" to subscription.id
        )
    }

    override suspend fun getUserByName(userName: String): GetUsersDataPayload? {
        val payload = "https://api.twitch.tv/helix/users".get<GetUsersPayload>(
            "login" to userName
        )

        return payload.data.firstOrNull()
    }

    override suspend fun getUserById(userId: UserId): GetUsersDataPayload? {
        val payload = "https://api.twitch.tv/helix/users".get<GetUsersPayload>(
            "id" to userId
        )

        return payload.data.firstOrNull()
    }

    override suspend fun getChannelInformation(userId: UserId): ChannelInformation? {
        val payload = "https://api.twitch.tv/helix/channels".get<ChannelInformationPayload>(
            "broadcaster_id" to userId.id
        )

        return payload.data.firstOrNull()
    }

    override suspend fun getVideosOf(userId: String, type: ChannelVideoType): ChannelVideosPayload {
        return authorizeCall { token ->
            httpClient.get("https://api.twitch.tv/helix/videos?user_id=$userId&type=$type") {
                header("Authorization", "Bearer ${token.access_token}")
                header("Client-Id", clientId)
                header("Accept", "application/vnd.twitchtv.v5+json")
            }.body()
        }
    }
}