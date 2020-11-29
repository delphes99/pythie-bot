package fr.delphes.twitch

import fr.delphes.twitch.api.reward.Reward
import fr.delphes.twitch.api.games.payload.GetGamesDataPayload
import fr.delphes.twitch.api.streams.payload.StreamInfos
import fr.delphes.twitch.api.user.payload.GetUsersDataPayload
import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.GenericCondition

interface ChannelHelixApi {
    suspend fun getGameByName(name: String): GetGamesDataPayload?

    suspend fun getGameById(id: String): GetGamesDataPayload?

    suspend fun getUser(userName: String): GetUsersDataPayload?

    suspend fun getStreamByUserId(userId: String): StreamInfos?

    suspend fun updateCustomReward(reward: Reward, activate: Boolean, userId: String)

    suspend fun subscribeEventSub(subscribe: EventSubSubscribe<out GenericCondition>)
}