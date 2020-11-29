package fr.delphes.twitch

import fr.delphes.twitch.api.channelUpdate.ChannelUpdate
import fr.delphes.twitch.auth.TwitchAppCredential
import fr.delphes.twitch.auth.TwitchUserCredential
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.reward.Reward
import fr.delphes.twitch.api.reward.RewardRedemption
import fr.delphes.twitch.api.games.SimpleGameId
import fr.delphes.twitch.api.newFollow.NewFollow
import fr.delphes.twitch.api.channelUpdate.ChannelUpdateEventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.api.newFollow.NewFollowEventSubConfiguration
import fr.delphes.twitch.api.streamOffline.StreamOffline
import fr.delphes.twitch.api.streamOffline.StreamOfflineEventSubConfiguration
import fr.delphes.twitch.api.streamOnline.StreamOnline
import fr.delphes.twitch.api.streamOnline.StreamOnlineEventSubConfiguration
import fr.delphes.twitch.model.Stream
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChannelTwitchClient(
    private val helixApi: ChannelHelixApi,
    private val pubSubApi: PubSubApi,
    private val webhookApi: WebhookApi,
    override val userId: String
) : ChannelTwitchApi, WebhookApi by webhookApi {
    override suspend fun getStream(): Stream? {
        val stream = helixApi.getStreamByUserId(userId) ?: return null
        val game = getGame(SimpleGameId(stream.game_id))

        return Stream(stream.title, stream.started_at, game)
    }

    override suspend fun getGame(id: GameId): Game {
        val game = helixApi.getGameById(id.id)

        return Game(SimpleGameId(game!!.id), game.name)
    }

    override suspend fun deactivateReward(reward: Reward) {
        helixApi.updateCustomReward(reward, false, userId)
    }

    override suspend fun activateReward(reward: Reward) {
        helixApi.updateCustomReward(reward, true, userId)
    }

    companion object {
        fun builder(
            appCredential: TwitchAppCredential,
            userCredential: TwitchUserCredential,
            userName: String,
            publicUrl: String,
            webhookSecret: String
        ): Builder {
            return Builder(appCredential, userCredential, userName, publicUrl, webhookSecret)
        }
    }

    class Builder(
        private val appCredential: TwitchAppCredential,
        private val userCredential: TwitchUserCredential,
        private val userName: String,
        private val publicUrl: String,
        private val webhookSecret: String
    ) {
        private var listenReward: ((RewardRedemption) -> Unit)? = null
        private val eventSubConfigurations = mutableListOf<EventSubConfiguration<*, *, *>>()

        fun listenToReward(listener: (RewardRedemption) -> Unit): Builder {
            listenReward = listener
            return this
        }

        fun listenToNewFollow(listener: (NewFollow) -> Unit): Builder {
            eventSubConfigurations.add(
                NewFollowEventSubConfiguration(
                    listener
                )
            )

            return this
        }

        fun listenToChannelUpdate(listener: (ChannelUpdate) -> Unit): Builder {
            eventSubConfigurations.add(
                ChannelUpdateEventSubConfiguration(
                    listener
                )
            )

            return this
        }

        fun listenToStreamOnline(listener: (StreamOnline) -> Unit): Builder {
            eventSubConfigurations.add(
                StreamOnlineEventSubConfiguration(
                    listener
                )
            )

            return this
        }

        fun listenToStreamOffline(listener: (StreamOffline) -> Unit): Builder {
            eventSubConfigurations.add(
                StreamOfflineEventSubConfiguration(
                    listener
                )
            )

            return this
        }

        fun build(): ChannelTwitchClient {
            val helixApi = ChannelHelixClient(appCredential, userCredential)

            val userId = runBlocking {
                helixApi.getUser(userName)!!.id
            }

            val pubSubApi = PubSubClient(userCredential, userId, listenReward)

            //TODO move somewhere else
            GlobalScope.launch {
                pubSubApi.listen()
            }

            val webhookApi = WebhookClient(
                publicUrl,
                userName,
                userId,
                webhookSecret,
                helixApi,
                eventSubConfigurations
            )

            return ChannelTwitchClient(
                helixApi,
                pubSubApi,
                webhookApi,
                userId
            )
        }
    }
}