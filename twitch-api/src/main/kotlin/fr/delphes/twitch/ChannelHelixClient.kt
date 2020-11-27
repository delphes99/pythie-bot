package fr.delphes.twitch

import fr.delphes.twitch.auth.TwitchAppCredential
import fr.delphes.twitch.auth.TwitchUserCredential
import fr.delphes.twitch.model.Reward
import fr.delphes.twitch.payload.games.GetGamesDataPayload
import fr.delphes.twitch.payload.games.GetGamesPayload
import fr.delphes.twitch.payload.reward.UpdateCustomRewardPayload
import fr.delphes.twitch.payload.streams.StreamInfos
import fr.delphes.twitch.payload.streams.StreamPayload
import fr.delphes.twitch.payload.users.GetUsersDataPayload
import fr.delphes.twitch.payload.users.GetUsersPayload
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

    override suspend fun updateCustomReward(reward: Reward, activate: Boolean, userId: String) {
        "https://api.twitch.tv/helix/channel_points/custom_rewards".patch<HttpResponse>(
            UpdateCustomRewardPayload(activate),
            userCredential,
            "broadcaster_id" to userId,
            "id" to reward.rewardId
        )
    }
}