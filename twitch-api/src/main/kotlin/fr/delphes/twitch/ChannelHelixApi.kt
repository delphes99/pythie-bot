package fr.delphes.twitch

import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardRedemption
import fr.delphes.twitch.api.channelPoll.CreatePoll
import fr.delphes.twitch.api.channelPoll.payload.CreatePollDataPayload
import fr.delphes.twitch.api.clips.payload.GetClipsPayload
import fr.delphes.twitch.api.games.payload.GetGamesDataPayload
import fr.delphes.twitch.api.reward.payload.CreateCustomReward
import fr.delphes.twitch.api.reward.payload.RedemptionStatusForUpdate
import fr.delphes.twitch.api.reward.payload.UpdateCustomReward
import fr.delphes.twitch.api.reward.payload.getCustomReward.GetCustomRewardDataPayload
import fr.delphes.twitch.api.streams.payload.StreamInfos
import java.time.LocalDateTime

interface ChannelHelixApi {
    suspend fun getGameByName(name: String): GetGamesDataPayload?

    suspend fun getGameById(id: String): GetGamesDataPayload?

    suspend fun getStream(): StreamInfos?

    suspend fun getCustomRewards(): List<GetCustomRewardDataPayload>

    suspend fun updateCustomReward(reward: UpdateCustomReward, rewardId: String)

    suspend fun createCustomReward(reward: CreateCustomReward) : GetCustomRewardDataPayload

    suspend fun updateRewardRedemption(redemption: RewardRedemption, status: RedemptionStatusForUpdate)

    suspend fun getClips(startAfter: LocalDateTime): List<GetClipsPayload>

    suspend fun createPoll(poll: CreatePoll): CreatePollDataPayload
}