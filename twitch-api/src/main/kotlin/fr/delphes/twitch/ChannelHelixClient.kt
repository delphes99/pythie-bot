package fr.delphes.twitch

import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardRedemption
import fr.delphes.twitch.api.games.payload.GetGamesDataPayload
import fr.delphes.twitch.api.games.payload.GetGamesPayload
import fr.delphes.twitch.api.reward.payload.CreateCustomReward
import fr.delphes.twitch.api.reward.payload.RedemptionStatusForUpdate
import fr.delphes.twitch.api.reward.payload.UpdateCustomRewardPayload
import fr.delphes.twitch.api.reward.payload.UpdateRedemptionStatus
import fr.delphes.twitch.api.reward.payload.getCustomReward.GetCustomRewardDataPayload
import fr.delphes.twitch.api.reward.payload.getCustomReward.GetCustomRewardPayload
import fr.delphes.twitch.api.streams.payload.StreamInfos
import fr.delphes.twitch.api.streams.payload.StreamPayload
import fr.delphes.twitch.api.user.payload.GetUsersDataPayload
import fr.delphes.twitch.api.user.payload.GetUsersPayload
import fr.delphes.twitch.auth.TwitchAppCredential
import fr.delphes.twitch.auth.TwitchUserCredential
import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.GenericCondition
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

    override suspend fun getCustomRewards(userId: String): List<GetCustomRewardDataPayload> {
        val payload = "https://api.twitch.tv/helix/channel_points/custom_rewards".get<GetCustomRewardPayload>(
            userCredential,
            "broadcaster_id" to userId
        )

        return payload.data
    }

    override suspend fun createCustomReward(reward: CreateCustomReward, userId: String): GetCustomRewardDataPayload {
        val payload = "https://api.twitch.tv/helix/channel_points/custom_rewards".post<GetCustomRewardPayload>(
            reward,
            userCredential,
            "broadcaster_id" to userId
        )
        return payload.data.first()
    }

    override suspend fun updateCustomReward(reward: UpdateCustomRewardPayload, rewardId: String, activate: Boolean, userId: String) {
        "https://api.twitch.tv/helix/channel_points/custom_rewards".patch<HttpResponse>(
            reward,
            userCredential,
            "broadcaster_id" to userId,
            "id" to rewardId
        )
    }

    override suspend fun updateRewardRedemption(redemption: RewardRedemption, userId: String, status: RedemptionStatusForUpdate) {
        "https://api.twitch.tv/helix/channel_points/custom_rewards/redemptions".patch<HttpResponse>(
            UpdateRedemptionStatus(status),
            userCredential,
            "id" to redemption.id,
            "broadcaster_id" to userId,
            "reward_id" to redemption.reward.id
        )
    }

    override suspend fun subscribeEventSub(subscribe: EventSubSubscribe<out GenericCondition>) {
        //TODO manage errors
        "https://api.twitch.tv/helix/eventsub/subscriptions".post<HttpResponse>(subscribe, appCredential)
    }
}