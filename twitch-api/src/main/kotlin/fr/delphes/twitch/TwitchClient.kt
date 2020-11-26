package fr.delphes.twitch

import fr.delphes.twitch.auth.TwitchAppCredential
import fr.delphes.twitch.auth.TwitchUserCredential
import fr.delphes.twitch.model.Game
import fr.delphes.twitch.model.GameId
import fr.delphes.twitch.model.Reward
import fr.delphes.twitch.model.RewardRedemption
import fr.delphes.twitch.model.SimpleGameId
import fr.delphes.twitch.model.Stream
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TwitchClient(
    private val helixApi: HelixApi,
    private val pubSubApi: PubSubApi,
    override val userId: String
) : TwitchApi {
    override suspend fun getStream(userId: String): Stream? {
        val stream = helixApi.getStreamByUserId(userId) ?: return null
        val game = getGame(SimpleGameId(stream.game_id))

        return Stream(stream.title, stream.started_at, game)
    }

    override suspend fun getGame(id: GameId): Game {
        val game = helixApi.getGameById(id.id)

        return Game(SimpleGameId(game!!.id), game.name)
    }

    //TODO inject userId in client
    override suspend fun desactivateReward(reward: Reward, userId: String) {
        helixApi.updateCustomReward(reward, false, userId)
    }

    override suspend fun activateReward(reward: Reward, userId: String) {
        helixApi.updateCustomReward(reward, true, userId)
    }

    companion object {
        fun builder(
            appCredential: TwitchAppCredential,
            userCredential: TwitchUserCredential,
            userName: String
        ): Builder {
            return Builder(appCredential, userCredential, userName)
        }
    }

    class Builder(
        private val appCredential: TwitchAppCredential,
        private val userCredential: TwitchUserCredential,
        private val userName: String
    ) {
        private var listenReward: ((RewardRedemption) -> Unit)? = null

        fun listenToReward(listener: (RewardRedemption) -> Unit): Builder {
            listenReward = listener
            return this
        }

        fun build(): TwitchClient {
            val helixApi = HelixClient(appCredential, userCredential)

            val userId = runBlocking {
                helixApi.getUser(userName)!!.id
            }

            val pubSubApi = PubSubClient(userCredential, userId, listenReward)

            //TODO move somewhere else
            GlobalScope.launch {
                pubSubApi.listen()
            }

            return TwitchClient(
                helixApi,
                pubSubApi,
                userId
            )
        }
    }
}