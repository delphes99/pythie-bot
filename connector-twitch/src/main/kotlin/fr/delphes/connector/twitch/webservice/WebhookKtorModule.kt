package fr.delphes.connector.twitch.webservice

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.eventMapper.ChannelBitsMapper
import fr.delphes.connector.twitch.eventMapper.ChannelUpdateMapper
import fr.delphes.connector.twitch.eventMapper.IncomingRaidMapper
import fr.delphes.connector.twitch.eventMapper.NewFollowMapper
import fr.delphes.connector.twitch.eventMapper.NewSubMapper
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
import fr.delphes.utils.exhaustive
import io.ktor.application.Application
import io.ktor.routing.Routing
import io.ktor.routing.routing

fun Application.WebhookModule(connector: TwitchConnector) {
    val rewardRedeemedMapper = RewardRedeemedMapper()
    val newFollowMapper = NewFollowMapper(connector)
    val newSubMapper = NewSubMapper(connector)
    val incomingRaidMapper = IncomingRaidMapper()
    val channelBitsMapper = ChannelBitsMapper(connector)
    val streamOnlineMapper = StreamOnlineMapper(connector)
    val streamOfflineMapper = StreamOfflineMapper(connector)
    val channelUpdateMapper = ChannelUpdateMapper(connector)


    routing {
        EventSubTopic.values()
            .forEach { topic ->
                when (topic) {
                    EventSubTopic.CHANNEL_UPDATE -> {
                        definition(ChannelUpdateEventSubConfiguration(), channelUpdateMapper, connector)
                    }
                    EventSubTopic.CUSTOM_REWARD_REDEMPTION -> {
                        definition(CustomRewardRedemptionEventSubConfiguration(), rewardRedeemedMapper, connector)
                    }
                    EventSubTopic.NEW_CHEER -> {
                        definition(ChannelCheerEventSubConfiguration(), channelBitsMapper, connector)
                    }
                    EventSubTopic.NEW_FOLLOW -> {
                        definition(ChannelFollowEventSubConfiguration(), newFollowMapper, connector)
                    }
                    EventSubTopic.NEW_SUB -> {
                        definition(ChannelSubscribeEventSubConfiguration(), newSubMapper, connector)
                    }
                    EventSubTopic.STREAM_OFFLINE -> {
                        definition(StreamOfflineEventSubConfiguration(), streamOfflineMapper, connector)
                    }
                    EventSubTopic.STREAM_ONLINE -> {
                        definition(StreamOnlineEventSubConfiguration(), streamOnlineMapper, connector)
                    }
                    EventSubTopic.CHANNEL_SUBSCRIPTION_MESSAGE -> {
                        definition(ChannelSubscriptionMessageEventSubConfiguration(), newSubMapper, connector)
                    }
                    EventSubTopic.INCOMING_RAID -> {
                        definition(ChannelRaidEventSubConfiguration(), incomingRaidMapper, connector)
                    }
                }.exhaustive()
            }
    }
}

private fun <MODEL, PAYLOAD, CONDITION : GenericCondition> Routing.definition(
    configuration: EventSubConfiguration<MODEL, PAYLOAD, CONDITION>,
    mapper: TwitchIncomingEventMapper<MODEL>,
    connector: TwitchConnector
) {
    configuration.callback.callbackDefinition(this) {
        mapper.mapAndHandle(connector, it)
    }
}

private suspend fun <T> TwitchIncomingEventMapper<T>.mapAndHandle(connector: TwitchConnector, event: T) {
    this.handle(event).forEach { mappedEvent ->
        connector.bot.handleIncomingEvent(mappedEvent)
    }
}