package fr.delphes.twitch

import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardRedemption
import fr.delphes.twitch.api.clips.payload.GetClips
import fr.delphes.twitch.api.clips.payload.GetClipsPayload
import fr.delphes.twitch.api.games.payload.GetGamesDataPayload
import fr.delphes.twitch.api.games.payload.GetGamesPayload
import fr.delphes.twitch.api.reward.payload.CreateCustomReward
import fr.delphes.twitch.api.reward.payload.RedemptionStatusForUpdate
import fr.delphes.twitch.api.reward.payload.UpdateCustomReward
import fr.delphes.twitch.api.reward.payload.UpdateRedemptionStatus
import fr.delphes.twitch.api.reward.payload.getCustomReward.GetCustomRewardDataPayload
import fr.delphes.twitch.api.reward.payload.getCustomReward.GetCustomRewardPayload
import fr.delphes.twitch.api.streams.payload.StreamInfos
import fr.delphes.twitch.api.streams.payload.StreamPayload
import fr.delphes.twitch.auth.CredentialsManager
import io.ktor.client.statement.HttpResponse
import java.time.LocalDateTime

internal class ChannelHelixClient(
    channel: TwitchChannel,
    clientId: String,
    credentialsManager: CredentialsManager,
    private val userId: String
) : ScopedHelixClient(
    clientId = clientId,
    getToken = { credentialsManager.getChannelToken(channel) ?: error("Token must be provided for channel ${channel.name}") },
    getTokenRefreshed = { credentialsManager.getChannelTokenRefreshed(channel) }
), ChannelHelixApi {
    override suspend fun getGameByName(name: String): GetGamesDataPayload? {
        val payload = "https://api.twitch.tv/helix/games".get<GetGamesPayload>(
            "name" to name
        )
        httpClient.attributes

        return payload.data.firstOrNull()
    }

    override suspend fun getGameById(id: String): GetGamesDataPayload? {
        val payload = "https://api.twitch.tv/helix/games".get<GetGamesPayload>(
            "id" to id
        )

        return payload.data.firstOrNull()
    }

    override suspend fun getStream(): StreamInfos? {
        val payload = "https://api.twitch.tv/helix/streams".get<StreamPayload>(
            "user_id" to userId
        )

        return payload.data.firstOrNull()
    }

    override suspend fun getCustomRewards(): List<GetCustomRewardDataPayload> {
        val payload = "https://api.twitch.tv/helix/channel_points/custom_rewards".get<GetCustomRewardPayload>(
            "broadcaster_id" to userId
        )

        return payload.data
    }

    override suspend fun createCustomReward(reward: CreateCustomReward): GetCustomRewardDataPayload {
        val payload = "https://api.twitch.tv/helix/channel_points/custom_rewards".post<GetCustomRewardPayload>(
            reward,
            "broadcaster_id" to userId
        )
        return payload.data.first()
    }

    override suspend fun updateCustomReward(reward: UpdateCustomReward, rewardId: String) {
        "https://api.twitch.tv/helix/channel_points/custom_rewards".patch<HttpResponse>(
            reward,
            "broadcaster_id" to userId,
            "id" to rewardId
        )
    }

    override suspend fun updateRewardRedemption(redemption: RewardRedemption, status: RedemptionStatusForUpdate) {
        "https://api.twitch.tv/helix/channel_points/custom_rewards/redemptions".patch<HttpResponse>(
            UpdateRedemptionStatus(status),
            "id" to redemption.id,
            "broadcaster_id" to userId,
            "reward_id" to redemption.reward.id
        )
    }

    override suspend fun getClips(startAfter: LocalDateTime): List<GetClipsPayload> {
        val payload = "https://api.twitch.tv/helix/clips".get<GetClips>(
            "broadcaster_id" to userId,
            "first" to 100,
            "started_at" to startAfter
        )

        return payload.data
    }
}