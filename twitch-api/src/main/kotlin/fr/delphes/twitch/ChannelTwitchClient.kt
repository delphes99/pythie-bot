package fr.delphes.twitch

import fr.delphes.twitch.api.channelCheer.ChannelCheerEventSubConfiguration
import fr.delphes.twitch.api.channelCheer.NewCheer
import fr.delphes.twitch.api.channelFollow.ChannelFollowEventSubConfiguration
import fr.delphes.twitch.api.channelFollow.NewFollow
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.CustomRewardRedemptionEventSubConfiguration
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardRedemption
import fr.delphes.twitch.api.channelSubscribe.ChannelSubscribeEventSubConfiguration
import fr.delphes.twitch.api.channelSubscribe.NewSub
import fr.delphes.twitch.api.channelUpdate.ChannelUpdate
import fr.delphes.twitch.api.channelUpdate.ChannelUpdateEventSubConfiguration
import fr.delphes.twitch.api.clips.Clip
import fr.delphes.twitch.api.clips.payload.GetClipsPayload
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.payload.UpdateCustomReward
import fr.delphes.twitch.api.streamOffline.StreamOffline
import fr.delphes.twitch.api.streamOffline.StreamOfflineEventSubConfiguration
import fr.delphes.twitch.api.streamOnline.StreamOnline
import fr.delphes.twitch.api.streamOnline.StreamOnlineEventSubConfiguration
import fr.delphes.twitch.api.streams.Stream
import fr.delphes.twitch.api.streams.ThumbnailUrl
import fr.delphes.twitch.api.user.TwitchUser
import fr.delphes.twitch.auth.CredentialsManager
import fr.delphes.twitch.clip.ClipCreated
import fr.delphes.twitch.clip.LastClipClient
import fr.delphes.twitch.eventSub.EventSubConfiguration
import mu.KotlinLogging
import java.time.Duration
import java.time.LocalDateTime

class ChannelTwitchClient(
    private val helixApi: ChannelHelixApi,
    private val webhookApi: WebhookApi,
    rewardsConfigurations: List<RewardConfiguration>
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

    companion object {
        private val LOGGER = KotlinLogging.logger {}

        fun builder(
            clientId: String,
            credentialsManager: CredentialsManager,
            user: TwitchUser,
            publicUrl: String,
            configFilepath: String,
            webhookSecret: String,
            rewardsConfigurations: List<RewardConfiguration>
        ): Builder {
            return Builder(clientId, credentialsManager, user, publicUrl, configFilepath, webhookSecret, rewardsConfigurations)
        }
    }

    class Builder(
        private val clientId: String,
        private val credentialsManager: CredentialsManager,
        private val user: TwitchUser,
        private val publicUrl: String,
        private val configFilepath: String,
        private val webhookSecret: String,
        private val rewardsConfigurations: List<RewardConfiguration>
    ) {
        val channel = TwitchChannel(user.name)

        private val eventSubConfigurations = mutableListOf<EventSubConfiguration<*, *, *>>()
        private var listenerClipCreated: (suspend (ClipCreated) -> Unit)? = null

        fun listenToReward(listener: suspend (RewardRedemption) -> Unit): Builder {
            eventSubConfigurations.add(
                CustomRewardRedemptionEventSubConfiguration(
                    channel,
                    listener
                )
            )

            return this
        }

        fun listenToNewFollow(listener: suspend (NewFollow) -> Unit): Builder {
            eventSubConfigurations.add(
                ChannelFollowEventSubConfiguration(
                    channel,
                    listener
                )
            )

            return this
        }

        fun listenToNewSub(listener: suspend (NewSub) -> Unit): Builder {
            eventSubConfigurations.add(
                ChannelSubscribeEventSubConfiguration(
                    channel,
                    listener
                )
            )

            return this
        }

        fun listenToNewCheer(listener: suspend (NewCheer) -> Unit): Builder {
            eventSubConfigurations.add(
                ChannelCheerEventSubConfiguration(
                    channel,
                    listener
                )
            )

            return this
        }

        fun listenToChannelUpdate(listener: suspend (ChannelUpdate) -> Unit): Builder {
            eventSubConfigurations.add(
                ChannelUpdateEventSubConfiguration(
                    channel,
                    listener
                )
            )

            return this
        }

        fun listenToStreamOnline(listener: suspend (StreamOnline) -> Unit): Builder {
            eventSubConfigurations.add(
                StreamOnlineEventSubConfiguration(
                    channel,
                    listener
                )
            )

            return this
        }

        fun listenToStreamOffline(listener: suspend (StreamOffline) -> Unit): Builder {
            eventSubConfigurations.add(
                StreamOfflineEventSubConfiguration(
                    channel,
                    listener
                )
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
                rewardsConfigurations
            )
        }
    }
}