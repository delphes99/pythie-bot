package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.bot.event.outgoing.WithBuilder
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.state.ChannelRewardsState
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.OutgoingEventType
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import fr.delphes.state.StateManager
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.WithRewardConfiguration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ActivateReward(
    val reward: WithRewardConfiguration,
    override val channel: TwitchChannel,
) : TwitchApiOutgoingEvent {
    override suspend fun executeOnTwitch(
        twitchApi: ChannelTwitchApi,
        connector: TwitchConnector,
    ) {
        twitchApi.activateReward(reward.rewardConfiguration)
    }

    companion object : WithBuilder {
        override val type = OutgoingEventType("twitch-activate-reward")

        override val builderDefinition = buildDefinition(::Builder)

        @Serializable
        @SerialName("twitch-activate-reward")
        class Builder(
            val rewardName: String = "",
            val channel: TwitchChannel = TwitchChannel(""),
        ) : OutgoingEventBuilder {
            override suspend fun build(stateManager: StateManager): ActivateReward {
                val channelRewardsState = stateManager.getStateOrNull(ChannelRewardsState.id(channel))
                    ?: error("No state for channel $channel")

                val reward = channelRewardsState.getReward(rewardName)
                    ?: error("No reward $rewardName for channel $channel")

                return ActivateReward(reward, channel)
            }

            override fun description(): OutgoingEventBuilderDescription {
                return buildDescription(
                    StringFeatureDescriptor(
                        fieldName = "rewardName",
                        description = "Reward to activate",
                        value = rewardName
                    ),
                    StringFeatureDescriptor(
                        fieldName = "channel",
                        description = "Channel owner of the reward",
                        value = channel.name
                    )
                )
            }
        }
    }
}