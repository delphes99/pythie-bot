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
import fr.delphes.twitch.auth.TwitchAppCredential
import fr.delphes.twitch.auth.TwitchUserCredential
import fr.delphes.twitch.eventSub.EventSubConfiguration
import mu.KotlinLogging
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
        return helixApi.getClips(startedAfter).map { payload ->
            Clip(
                payload.url,
                TwitchUser(payload.creator_id, payload.creator_name),
                payload.game_id, //TODO game repository to retrieve game name
                payload.title,
                payload.created_at,
                ThumbnailUrl(payload.thumbnail_url)
            )
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}

        fun builder(
            appCredential: TwitchAppCredential,
            userCredential: TwitchUserCredential,
            user: TwitchUser,
            publicUrl: String,
            webhookSecret: String,
            rewardsConfigurations: List<RewardConfiguration>
        ): Builder {
            return Builder(appCredential, userCredential, user, publicUrl, webhookSecret, rewardsConfigurations)
        }
    }

    class Builder(
        private val appCredential: TwitchAppCredential,
        private val userCredential: TwitchUserCredential,
        private val user: TwitchUser,
        private val publicUrl: String,
        private val webhookSecret: String,
        private val rewardsConfigurations: List<RewardConfiguration>
    ) {
        val channel = TwitchChannel(user.name)

        private val eventSubConfigurations = mutableListOf<EventSubConfiguration<*, *, *>>()

        fun listenToReward(listener: (RewardRedemption) -> Unit): Builder {
            eventSubConfigurations.add(
                CustomRewardRedemptionEventSubConfiguration(
                    channel,
                    listener,
                    rewardsConfigurations
                )
            )

            return this
        }

        fun listenToNewFollow(listener: (NewFollow) -> Unit): Builder {
            eventSubConfigurations.add(
                ChannelFollowEventSubConfiguration(
                    channel,
                    listener
                )
            )

            return this
        }

        fun listenToNewSub(listener: (NewSub) -> Unit): Builder {
            eventSubConfigurations.add(
                ChannelSubscribeEventSubConfiguration(
                    channel,
                    listener
                )
            )

            return this
        }

        fun listenToNewCheer(listener: (NewCheer) -> Unit): Builder {
            eventSubConfigurations.add(
                ChannelCheerEventSubConfiguration(
                    channel,
                    listener
                )
            )

            return this
        }

        fun listenToChannelUpdate(listener: (ChannelUpdate) -> Unit): Builder {
            eventSubConfigurations.add(
                ChannelUpdateEventSubConfiguration(
                    channel,
                    listener
                )
            )

            return this
        }

        fun listenToStreamOnline(listener: (StreamOnline) -> Unit): Builder {
            eventSubConfigurations.add(
                StreamOnlineEventSubConfiguration(
                    channel,
                    listener
                )
            )

            return this
        }

        fun listenToStreamOffline(listener: (StreamOffline) -> Unit): Builder {
            eventSubConfigurations.add(
                StreamOfflineEventSubConfiguration(
                    channel,
                    listener
                )
            )

            return this
        }

        fun build(): ChannelTwitchClient {
            val helixApi = ChannelHelixClient(appCredential, userCredential, user.id)

            val webhookApi = WebhookClient(
                publicUrl,
                user,
                webhookSecret,
                AppHelixClient(appCredential),
                eventSubConfigurations
            )

            return ChannelTwitchClient(
                helixApi,
                webhookApi,
                rewardsConfigurations
            )
        }
    }
}