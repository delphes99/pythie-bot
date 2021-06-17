package fr.delphes.connector.twitch.webservice

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.eventMapper.ChannelBitsMapper
import fr.delphes.connector.twitch.eventMapper.ChannelUpdateMapper
import fr.delphes.connector.twitch.eventMapper.IncomingRaidMapper
import fr.delphes.connector.twitch.eventMapper.NewFollowMapper
import fr.delphes.connector.twitch.eventMapper.NewSubMapper
import fr.delphes.connector.twitch.eventMapper.NewSubMessageMapper
import fr.delphes.connector.twitch.eventMapper.RewardRedeemedMapper
import fr.delphes.connector.twitch.eventMapper.StreamOfflineMapper
import fr.delphes.connector.twitch.eventMapper.StreamOnlineMapper
import fr.delphes.connector.twitch.eventMapper.TwitchIncomingEventMapper
import fr.delphes.twitch.api.channelCheer.ChannelCheerEventSubConfiguration
import fr.delphes.twitch.api.channelFollow.ChannelFollowEventSubConfiguration
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.CustomRewardRedemptionEventSubConfiguration
import fr.delphes.twitch.api.channelRaid.ChannelRaidEventSubConfiguration
import fr.delphes.twitch.api.channelSubscribe.ChannelSubscribeEventSubConfiguration
import fr.delphes.twitch.api.channelSubscribe.ChannelSubscriptionMessageEventSubConfiguration
import fr.delphes.twitch.api.channelUpdate.ChannelUpdateEventSubConfiguration
import fr.delphes.twitch.api.streamOffline.StreamOfflineEventSubConfiguration
import fr.delphes.twitch.api.streamOnline.StreamOnlineEventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.GenericCondition
import io.ktor.application.Application
import io.ktor.routing.Routing
import io.ktor.routing.routing

fun Application.WebhookModule(connector: TwitchConnector) {
    routing {
        EventSubTopic.values()
            .forEach { topic ->
                val configuration = when (topic) {
                    EventSubTopic.CHANNEL_UPDATE -> {
                        Configuration(ChannelUpdateEventSubConfiguration(), ChannelUpdateMapper(connector))
                    }
                    EventSubTopic.CUSTOM_REWARD_REDEMPTION -> {
                        Configuration(CustomRewardRedemptionEventSubConfiguration(), RewardRedeemedMapper())
                    }
                    EventSubTopic.NEW_CHEER -> {
                        Configuration(ChannelCheerEventSubConfiguration(), ChannelBitsMapper())
                    }
                    EventSubTopic.NEW_FOLLOW -> {
                        Configuration(ChannelFollowEventSubConfiguration(), NewFollowMapper())
                    }
                    EventSubTopic.NEW_SUB -> {
                        Configuration(ChannelSubscribeEventSubConfiguration(), NewSubMapper())
                    }
                    EventSubTopic.STREAM_OFFLINE -> {
                        Configuration(StreamOfflineEventSubConfiguration(), StreamOfflineMapper())
                    }
                    EventSubTopic.STREAM_ONLINE -> {
                        Configuration(StreamOnlineEventSubConfiguration(), StreamOnlineMapper(connector))
                    }
                    EventSubTopic.CHANNEL_SUBSCRIPTION_MESSAGE -> {
                        Configuration(ChannelSubscriptionMessageEventSubConfiguration(), NewSubMessageMapper())
                    }
                    EventSubTopic.INCOMING_RAID -> {
                        Configuration(ChannelRaidEventSubConfiguration(), IncomingRaidMapper())
                    }
                }

                configuration.defineCallBack(this, connector)
            }
    }
}

private data class Configuration<PAYLOAD, CONDITION : GenericCondition>(
    val configuration: EventSubConfiguration<PAYLOAD, CONDITION>,
    val mapper: TwitchIncomingEventMapper<PAYLOAD>,
) {
    fun defineCallBack(routing: Routing, connector: TwitchConnector) {
        configuration.callback.callbackDefinition(routing) {
            mapper.handle(it).forEach { mappedEvent ->
                connector.handleIncomingEvent(mappedEvent)
            }
        }
    }
}
