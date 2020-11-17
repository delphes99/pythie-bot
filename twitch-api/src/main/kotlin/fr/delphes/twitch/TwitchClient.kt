package fr.delphes.twitch

import fr.delphes.twitch.model.Game
import fr.delphes.twitch.model.GameId
import fr.delphes.twitch.model.SimpleGameId
import fr.delphes.twitch.model.Stream
import kotlinx.coroutines.runBlocking

class TwitchClient(
    private val helixApi: HelixApi,
    private val pubSubApi: PubSubApi,
    override val userId: String
): TwitchApi {
    override suspend fun getStream(userId: String): Stream? {
        val stream = helixApi.getStreamByUserId(userId) ?: return null
        val game = getGame(SimpleGameId(stream.game_id))

        return Stream(stream.title, stream.started_at, game)
    }

    override suspend fun getGame(id: GameId): Game {
        val game = helixApi.getGameById(id.id)

        return Game(SimpleGameId(game!!.id), game.name)
    }

    companion object {
        fun build(clientId: String, authToken: String, userName: String): TwitchClient {
            val helixApi = HelixClient(clientId, authToken)

            val userId = runBlocking {
                helixApi.getUser(userName)!!.id
            }

            return TwitchClient(
                helixApi,
                PubSubClient(authToken, userId),
                userId
            )
        }
    }
}