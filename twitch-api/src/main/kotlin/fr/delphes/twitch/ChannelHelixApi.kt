package fr.delphes.twitch

import fr.delphes.twitch.model.Reward
import fr.delphes.twitch.payload.games.GetGamesDataPayload
import fr.delphes.twitch.payload.streams.StreamInfos
import fr.delphes.twitch.payload.users.GetUsersDataPayload

interface ChannelHelixApi {
    suspend fun getGameByName(name: String): GetGamesDataPayload?

    suspend fun getGameById(id: String): GetGamesDataPayload?

    suspend fun getUser(userName: String): GetUsersDataPayload?

    suspend fun getStreamByUserId(userId: String): StreamInfos?

    suspend fun updateCustomReward(reward: Reward, activate: Boolean, userId: String)
}