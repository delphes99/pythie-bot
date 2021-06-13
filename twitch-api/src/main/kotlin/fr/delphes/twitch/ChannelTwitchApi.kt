package fr.delphes.twitch

import fr.delphes.twitch.api.channelPoll.CreatePoll
import fr.delphes.twitch.api.clips.Clip
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.streams.Stream
import java.time.LocalDateTime

interface ChannelTwitchApi : WebhookApi {
    suspend fun getStream(): Stream?

    suspend fun getGame(id: GameId): Game

    suspend fun deactivateReward(reward: RewardConfiguration)

    suspend fun activateReward(reward: RewardConfiguration)

    suspend fun getClips(startedAfter: LocalDateTime): List<Clip>

    suspend fun createPoll(poll: CreatePoll)
}