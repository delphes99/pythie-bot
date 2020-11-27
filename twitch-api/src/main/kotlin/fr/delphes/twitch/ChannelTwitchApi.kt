package fr.delphes.twitch

import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.reward.Reward
import fr.delphes.twitch.model.Stream

interface ChannelTwitchApi {
    val userId: String

    suspend fun getStream(): Stream?

    suspend fun getGame(id: GameId): Game

    suspend fun deactivateReward(reward: Reward)

    suspend fun activateReward(reward: Reward)
}