package fr.delphes.twitch

import fr.delphes.twitch.api.channelUpdate.payload.ChannelUpdateCondition
import fr.delphes.twitch.api.channelUpdate.payload.SubscribeChannelUpdate
import fr.delphes.twitch.auth.TwitchAppCredential
import fr.delphes.twitch.auth.TwitchUserCredential
import fr.delphes.twitch.api.reward.Reward
import fr.delphes.twitch.api.games.payload.GetGamesDataPayload
import fr.delphes.twitch.api.games.payload.GetGamesPayload
import fr.delphes.twitch.api.reward.payload.UpdateCustomRewardPayload
import fr.delphes.twitch.api.streams.payload.StreamInfos
import fr.delphes.twitch.api.streams.payload.StreamPayload
import fr.delphes.twitch.api.user.payload.GetUsersDataPayload
import fr.delphes.twitch.api.user.payload.GetUsersPayload
import fr.delphes.twitch.api.newFollow.payload.NewFollowCondition
import fr.delphes.twitch.api.newFollow.payload.SubscribeNewFollow
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.client.statement.HttpResponse

internal class ChannelHelixClient(
    appCredential: TwitchAppCredential,
    private val userCredential: TwitchUserCredential
) : AbstractHelixClient(appCredential), ChannelHelixApi {
    override suspend fun getGameByName(name: String): GetGamesDataPayload? {
        val payload = "https://api.twitch.tv/helix/games".get<GetGamesPayload>(
            userCredential,
            "name" to name
        )
        httpClient.attributes

        return payload.data.firstOrNull()
    }

    override suspend fun getGameById(id: String): GetGamesDataPayload? {
        val payload = "https://api.twitch.tv/helix/games".get<GetGamesPayload>(
            userCredential,
            "id" to id
        )

        return payload.data.firstOrNull()
    }

    override suspend fun getUser(userName: String): GetUsersDataPayload? {
        val payload = "https://api.twitch.tv/helix/users".get<GetUsersPayload>(
            userCredential,
            "login" to userName
        )

        return payload.data.firstOrNull()
    }

    override suspend fun getStreamByUserId(userId: String): StreamInfos? {
        val payload = "https://api.twitch.tv/helix/streams".get<StreamPayload>(
            userCredential,
            "user_id" to userId
        )

        return payload.data.firstOrNull()
    }

    override suspend fun updateCustomReward(reward: Reward, activate: Boolean, userId: String) {
        "https://api.twitch.tv/helix/channel_points/custom_rewards".patch<HttpResponse>(
            UpdateCustomRewardPayload(activate),
            userCredential,
            "broadcaster_id" to userId,
            "id" to reward.rewardId
        )
    }

    override suspend fun subscribeEventSub(
        channelFollow: EventSubSubscriptionType,
        callback: String,
        userId: String,
        secret: String
    ) {
        //TODO manage errors
        @Suppress("IMPLICIT_CAST_TO_ANY") val payload = when (channelFollow) {
            EventSubSubscriptionType.CHANNEL_FOLLOW -> {
                SubscribeNewFollow(
                    NewFollowCondition(userId),
                    SubscribeTransport(
                        callback,
                        secret
                    )
                )
            }
            EventSubSubscriptionType.CHANNEL_UPDATE -> {
                SubscribeChannelUpdate(
                    ChannelUpdateCondition(userId),
                    SubscribeTransport(
                        callback,
                        secret
                    )
                )
            }
        }
        "https://api.twitch.tv/helix/eventsub/subscriptions".post<HttpResponse>(payload, appCredential)
    }
}