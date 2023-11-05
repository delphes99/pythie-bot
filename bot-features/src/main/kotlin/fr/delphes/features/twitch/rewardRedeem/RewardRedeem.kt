package fr.delphes.features.twitch.rewardRedeem

import fr.delphes.bot.event.eventHandler.IncomingEventHandlerAction
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.features.twitch.handlersFor
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel

class RewardRedeem(
    override val channel: TwitchChannel,
    private val rewardId: RewardId,
    override val id: FeatureId = FeatureId(),
    private val action: IncomingEventHandlerAction<RewardRedemption>,
) : TwitchFeature, FeatureDefinition {
    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime {
        val eventHandlers = channel.handlersFor(RewardRedemption::class) {
            if (rewardId == event.data.rewardId) {
                action()
            }
        }

        return SimpleFeatureRuntime(eventHandlers, id)
    }

    override fun getSpecificStates(stateProvider: StateProvider): List<State> = emptyList()
}