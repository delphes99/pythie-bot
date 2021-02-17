package fr.delphes.twitch

import fr.delphes.twitch.api.user.payload.GetUsersDataPayload
import fr.delphes.twitch.api.user.payload.GetUsersPayload
import fr.delphes.twitch.auth.CredentialsManager
import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.GenericCondition
import fr.delphes.twitch.eventSub.payload.subscription.ListSubscriptionsPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscriptionPayload
import io.ktor.client.statement.HttpResponse

class AppHelixClient(
    clientId: String,
    credentialsManager: CredentialsManager
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
}