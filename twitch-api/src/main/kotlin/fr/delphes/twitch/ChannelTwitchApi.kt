package fr.delphes.twitch

import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.streams.Stream

interface ChannelTwitchApi : WebhookApi {
    val userId: String

    suspend fun getStream(): Stream?

    suspend fun getGame(id: GameId): Game

    suspend fun deactivateReward(reward: RewardConfiguration)

    suspend fun activateReward(reward: RewardConfiguration)
}