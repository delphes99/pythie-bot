package fr.delphes.twitch

import fr.delphes.twitch.api.channelPoll.CreatePoll
import fr.delphes.twitch.api.clips.Clip
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.reward.TwitchRewardConfiguration
import fr.delphes.twitch.api.reward.TwitchRewardId
import fr.delphes.twitch.api.reward.payload.CreateCustomReward
import fr.delphes.twitch.api.reward.payload.UpdateCustomReward
import fr.delphes.twitch.api.reward.payload.getCustomReward.GetCustomRewardDataPayload
import fr.delphes.twitch.api.streams.Stream
import fr.delphes.twitch.api.user.TwitchUser
import fr.delphes.twitch.api.user.UserId
import java.time.LocalDateTime

interface ChannelTwitchApi : WebhookApi {
    suspend fun getStream(): Stream?

    suspend fun getGame(id: GameId): Game?

    @Deprecated("Use getRewards instead (cache in connector)")
    suspend fun getCachedRewards(): List<TwitchRewardConfiguration>

    suspend fun getRewards(): List<GetCustomRewardDataPayload>

    suspend fun deactivateReward(id: TwitchRewardId)

    suspend fun activateReward(id: TwitchRewardId)

    suspend fun updateReward(reward: UpdateCustomReward, rewardId: TwitchRewardId)

    suspend fun createReward(reward: CreateCustomReward): GetCustomRewardDataPayload

    suspend fun getClips(startedAfter: LocalDateTime): List<Clip>

    suspend fun createPoll(poll: CreatePoll)

    suspend fun getVIPs(): List<TwitchUser>

    suspend fun removeVip(userId: UserId)

    suspend fun promoteVip(userId: UserId)

    suspend fun removeModerator(userId: UserId)

    suspend fun promoteModerator(userId: UserId)

    suspend fun sendShoutout(userId: UserId)
}