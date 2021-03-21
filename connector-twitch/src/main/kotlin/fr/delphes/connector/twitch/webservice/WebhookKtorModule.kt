package fr.delphes.connector.twitch.webservice

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.eventMapper.ChannelBitsMapper
import fr.delphes.connector.twitch.eventMapper.ChannelUpdateMapper
import fr.delphes.connector.twitch.eventMapper.NewFollowMapper
import fr.delphes.connector.twitch.eventMapper.NewSubMapper
import fr.delphes.connector.twitch.eventMapper.RewardRedeemedMapper
import fr.delphes.connector.twitch.eventMapper.StreamOfflineMapper
import fr.delphes.connector.twitch.eventMapper.StreamOnlineMapper
import fr.delphes.connector.twitch.eventMapper.TwitchIncomingEventMapper
import fr.delphes.twitch.api.channelCheer.ChannelCheerEventSubConfiguration
import fr.delphes.twitch.api.channelFollow.ChannelFollowEventSubConfiguration
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.CustomRewardRedemptionEventSubConfiguration
import fr.delphes.twitch.api.channelSubscribe.ChannelSubscribeEventSubConfiguration
import fr.delphes.twitch.api.channelUpdate.ChannelUpdateEventSubConfiguration
import fr.delphes.twitch.api.streamOffline.StreamOfflineEventSubConfiguration
import fr.delphes.twitch.api.streamOnline.StreamOnlineEventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import io.ktor.application.Application
import io.ktor.routing.routing

fun Application.WebhookModule(connector: TwitchConnector) {
    val rewardRedeemedMapper = RewardRedeemedMapper()
    val newFollowMapper = NewFollowMapper(connector)
    val newSubMapper = NewSubMapper(connector)
    val channelBitsMapper = ChannelBitsMapper(connector)
    val streamOnlineMapper = StreamOnlineMapper(connector)
    val streamOfflineMapper = StreamOfflineMapper(connector)
    val channelUpdateMapper = ChannelUpdateMapper(connector)

    routing {
        EventSubTopic.values()
            .map {
                when (it) {
                    EventSubTopic.CHANNEL_UPDATE -> ChannelUpdateEventSubConfiguration { channelUpdate ->
                        channelUpdateMapper.mapAndHandle(connector, channelUpdate)
                    }
                    EventSubTopic.CUSTOM_REWARD_REDEMPTION -> CustomRewardRedemptionEventSubConfiguration { redemption ->
                        rewardRedeemedMapper.mapAndHandle(connector, redemption)
                    }
                    EventSubTopic.NEW_CHEER -> ChannelCheerEventSubConfiguration { newCheer ->
                        channelBitsMapper.mapAndHandle(connector, newCheer)
                    }
                    EventSubTopic.NEW_FOLLOW -> ChannelFollowEventSubConfiguration { newFollow ->
                        newFollowMapper.mapAndHandle(connector, newFollow)
                    }
                    EventSubTopic.NEW_SUB -> ChannelSubscribeEventSubConfiguration { newSub ->
                        newSubMapper.mapAndHandle(connector, newSub)
                    }
                    EventSubTopic.STREAM_OFFLINE -> StreamOfflineEventSubConfiguration { streamOffline ->
                        streamOfflineMapper.mapAndHandle(connector, streamOffline)
                    }
                    EventSubTopic.STREAM_ONLINE -> StreamOnlineEventSubConfiguration { streamOnline ->
                        streamOnlineMapper.mapAndHandle(connector, streamOnline)
                    }
                }
            }
            .forEach { configuration ->
                configuration.callback.callbackDefinition(this)
            }
    }
}


private suspend fun <T> TwitchIncomingEventMapper<T>.mapAndHandle(connector: TwitchConnector, event: T) {
    this.handle(event).forEach { mappedEvent ->
        connector.bot.handleIncomingEvent(mappedEvent)
    }
}