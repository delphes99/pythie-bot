package fr.delphes.twitch

import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.Reward
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.model.Stream

interface ChannelTwitchApi : WebhookApi {
    val userId: String

    suspend fun getStream(): Stream?

    suspend fun getGame(id: GameId): Game

    suspend fun deactivateReward(reward: Reward)

    suspend fun activateReward(reward: Reward)
}