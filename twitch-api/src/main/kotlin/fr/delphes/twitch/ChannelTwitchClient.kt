package fr.delphes.twitch

import fr.delphes.twitch.api.channelCheer.ChannelCheerEventSubConfiguration
import fr.delphes.twitch.api.channelFollow.ChannelFollowEventSubConfiguration
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.CustomRewardRedemptionEventSubConfiguration
import fr.delphes.twitch.api.channelPoll.ChannelPollBeginEventSubConfiguration
import fr.delphes.twitch.api.channelPoll.ChannelPollEndEventSubConfiguration
import fr.delphes.twitch.api.channelPoll.ChannelPollProgressEventSubConfiguration
import fr.delphes.twitch.api.channelPoll.CreatePoll
import fr.delphes.twitch.api.channelPrediction.ChannelPredictionBeginEventSubConfiguration
import fr.delphes.twitch.api.channelPrediction.ChannelPredictionEndEventSubConfiguration
import fr.delphes.twitch.api.channelPrediction.ChannelPredictionLockEventSubConfiguration
import fr.delphes.twitch.api.channelPrediction.ChannelPredictionProgressEventSubConfiguration
import fr.delphes.twitch.api.channelRaid.ChannelRaidEventSubConfiguration
import fr.delphes.twitch.api.channelSubscribe.ChannelSubscribeEventSubConfiguration
import fr.delphes.twitch.api.channelSubscribe.ChannelSubscriptionMessageEventSubConfiguration
import fr.delphes.twitch.api.channelUpdate.ChannelUpdateEventSubConfiguration
import fr.delphes.twitch.api.clips.Clip
import fr.delphes.twitch.api.clips.payload.GetClipsPayload
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.payload.UpdateCustomReward
import fr.delphes.twitch.api.streamOffline.StreamOfflineEventSubConfiguration
import fr.delphes.twitch.api.streamOnline.StreamOnlineEventSubConfiguration
import fr.delphes.twitch.api.streams.Stream
import fr.delphes.twitch.api.streams.ThumbnailUrl
import fr.delphes.twitch.api.user.TwitchUser
import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.auth.CredentialsManager
import fr.delphes.twitch.clip.ClipCreated
import fr.delphes.twitch.clip.LastClipClient
import fr.delphes.twitch.eventSub.EventSubConfiguration
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import mu.KotlinLogging
import java.time.Duration
import java.time.LocalDateTime

class ChannelTwitchClient(
    private val helixApi: ChannelHelixApi,
    private val webhookApi: WebhookApi,
    rewardsConfigurations: List<RewardConfiguration>,
    private val appTwitchApi: AppTwitchApi
) : ChannelTwitchApi, WebhookApi by webhookApi {
    private val rewards = RewardCache(rewardsConfigurations, helixApi)

    override suspend fun getStream(): Stream? {
        val stream = helixApi.getStream() ?: return null
        val game = getGame(GameId(stream.game_id))

        return Stream(stream.id, stream.title, stream.started_at, game, ThumbnailUrl(stream.thumbnail_url))
    }

    override suspend fun getGame(id: GameId): Game {
        val game = helixApi.getGameById(id.id)

        return Game(GameId(game!!.id), game.name)
    }

    override suspend fun deactivateReward(reward: RewardConfiguration) {
        rewards.rewardOf(reward)?.also { twitchReward ->
            helixApi.updateCustomReward(UpdateCustomReward(is_enabled = false), twitchReward.id)
        } ?: LOGGER.error { "no twitch reward found : ${reward.title}" }
    }

    override suspend fun activateReward(reward: RewardConfiguration) {
        rewards.rewardOf(reward)?.also { twitchReward ->
            helixApi.updateCustomReward(UpdateCustomReward(is_enabled = true), twitchReward.id)
        } ?: LOGGER.error { "no twitch reward found : ${reward.title}" }
    }

    override suspend fun getClips(startedAfter: LocalDateTime): List<Clip> {
        return helixApi.getClips(startedAfter).map(GetClipsPayload::toClip)
    }

    override suspend fun createPoll(poll: CreatePoll) {
        helixApi.createPoll(poll)
    }

    override suspend fun getVIPs(): List<TwitchUser> {
        return coroutineScope {
            helixApi.getVIPs()
                .map { async { appTwitchApi.getUserById(UserId(it.userId)) } }
                .awaitAll()
                .filterNotNull()
        }
    }

    override suspend fun removeVip(user: TwitchUser) {
        helixApi.removeVip(user)
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}

        fun builder(
            appTwitchApi: AppTwitchApi,
            clientId: String,
            credentialsManager: CredentialsManager,
            user: TwitchUser,
            publicUrl: String,
            configFilepath: String,
            webhookSecret: String,
            rewardsConfigurations: List<RewardConfiguration>
        ): Builder {
            return Builder(
                appTwitchApi,
                clientId,
                credentialsManager,
                user,
                publicUrl,
                configFilepath,
                webhookSecret,
                rewardsConfigurations
            )
        }
    }

    class Builder(
        private val appTwitchApi: AppTwitchApi,
        private val clientId: String,
        private val credentialsManager: CredentialsManager,
        private val user: TwitchUser,
        private val publicUrl: String,
        private val configFilepath: String,
        private val webhookSecret: String,
        private val rewardsConfigurations: List<RewardConfiguration>
    ) {
        val channel = TwitchChannel(user.name)

        private val eventSubConfigurations = mutableListOf<EventSubConfiguration<*, *>>()
        private var listenerClipCreated: (suspend (ClipCreated) -> Unit)? = null

        fun listenToReward(): Builder {
            eventSubConfigurations.add(
                CustomRewardRedemptionEventSubConfiguration()
            )

            return this
        }

        fun listenToNewFollow(): Builder {
            eventSubConfigurations.add(
                ChannelFollowEventSubConfiguration()
            )

            return this
        }

        fun listenToNewSub(): Builder {
            eventSubConfigurations.add(
                ChannelSubscribeEventSubConfiguration()
            )
            //TODO same subscription ??
            eventSubConfigurations.add(
                ChannelSubscriptionMessageEventSubConfiguration()
            )

            return this
        }

        fun listenToNewCheer(): Builder {
            eventSubConfigurations.add(
                ChannelCheerEventSubConfiguration()
            )

            return this
        }

        fun listenToChannelUpdate(): Builder {
            eventSubConfigurations.add(
                ChannelUpdateEventSubConfiguration()
            )

            return this
        }

        fun listenToStreamOnline(): Builder {
            eventSubConfigurations.add(
                StreamOnlineEventSubConfiguration()
            )

            return this
        }

        fun listenToStreamOffline(): Builder {
            eventSubConfigurations.add(
                StreamOfflineEventSubConfiguration()
            )

            return this
        }

        fun listenToIncomingRaid(): Builder {
            eventSubConfigurations.add(
                ChannelRaidEventSubConfiguration()
            )

            return this
        }

        fun listenToPoll(): Builder {
            eventSubConfigurations.add(
                ChannelPollBeginEventSubConfiguration()
            )
            eventSubConfigurations.add(
                ChannelPollProgressEventSubConfiguration()
            )
            eventSubConfigurations.add(
                ChannelPollEndEventSubConfiguration()
            )

            return this
        }

        fun listenToPrediction(): Builder {
            eventSubConfigurations.add(
                ChannelPredictionBeginEventSubConfiguration()
            )
            eventSubConfigurations.add(
                ChannelPredictionProgressEventSubConfiguration()
            )
            eventSubConfigurations.add(
                ChannelPredictionLockEventSubConfiguration()
            )
            eventSubConfigurations.add(
                ChannelPredictionEndEventSubConfiguration()
            )

            return this
        }

        fun listenToClipCreated(listener: suspend (ClipCreated) -> Unit): Builder {
            listenerClipCreated = listener

            return this
        }

        fun build(): ChannelTwitchClient {

            val helixApi = ChannelHelixClient(channel, clientId, credentialsManager, user.id)

            val webhookApi = WebhookClient(
                publicUrl,
                user,
                webhookSecret,
                AppHelixClient(clientId, credentialsManager),
                eventSubConfigurations
            )

            val lastClipApi = LastClipClient(
                channel,
                helixApi,
                Duration.ofMinutes(1),
                configFilepath,
                listenerClipCreated
            )

            listenerClipCreated?.also {
                lastClipApi.listen()
            }

            return ChannelTwitchClient(
                helixApi,
                webhookApi,
                rewardsConfigurations,
                appTwitchApi
            )
        }
    }
}