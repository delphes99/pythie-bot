package fr.delphes.features.twitch.rewardRedeem

import fr.delphes.bot.event.eventHandler.EventHandlerContext
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.features.twitch.handlerFor
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.WithRewardConfiguration

class RewardRedeem(
    override val channel: TwitchChannel,
    private val rewardConfiguration: WithRewardConfiguration,
    override val id: FeatureId = FeatureId(),
    private val action: EventHandlerContext<RewardRedemption>,
) : TwitchFeature, FeatureDefinition {
    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime {
        val eventHandlers = channel.handlerFor(RewardRedemption::class) {
            if (rewardConfiguration.rewardConfiguration.match(event.reward)) {
                action()
            }
        }

        return SimpleFeatureRuntime(eventHandlers, id)
    }

    override fun getSpecificStates(stateProvider: StateProvider): List<State> = emptyList()
}