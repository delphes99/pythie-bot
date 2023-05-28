package fr.delphes.features.twitch.voth

import fr.delphes.bot.event.eventHandler.EventHandlerAction
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.connector.twitch.outgoingEvent.PromoteVIP
import fr.delphes.connector.twitch.outgoingEvent.RemoveVIP
import fr.delphes.connector.twitch.state.GetVipState
import fr.delphes.features.twitch.handlerFor
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.state.state.ClockState
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.WithRewardConfiguration
import fr.delphes.twitch.api.user.UserName

class VOTH(
    override val channel: TwitchChannel,
    val reward: WithRewardConfiguration,
    val newVipAnnouncer: EventHandlerAction<NewVOTHAnnounced>,
    private val saveStatePath: String? = null,
) : FeatureDefinition, TwitchFeature {
    override val id = idFor(channel)

    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime {
        val eventHandlers = EventHandlers
            .builder()
            .addHandler(
                channel.handlerFor<RewardRedemption> {
                    if (reward.rewardConfiguration.match(event.reward)) {
                        val state = state(VOTHState.idFor(channel)) ?: error("No state for VOTH")
                        val oldVOTH = state.data.currentVip
                        state.newVOTH(event)
                        val newVOTH = state.data.currentVip
                        if (newVOTH != null && newVOTH != oldVOTH) {
                            val removeAllVips = state(GetVipState.ID)
                                ?.getVipOf(channel)
                                ?.map { RemoveVIP(UserName(it.name), channel) }
                                ?: emptyList()

                            val promoteVIP = PromoteVIP(event.user, channel)

                            val alert = Alert("newVip", "newVip" to (newVOTH.user.name))

                            NewVOTHAnnounced(
                                oldVOTH,
                                newVOTH,
                                event
                            )
                                .newActionContext()
                                .newVipAnnouncer()

                            executeOutgoingEvent(promoteVIP)
                            executeOutgoingEvents(removeAllVips)
                            executeOutgoingEvent(alert)
                        }
                    }
                }
            )
            .addHandler(
                channel.handlerFor<StreamOnline> {
                    state(VOTHState.idFor(channel))?.unpause() ?: error("No state for VOTH")
                }
            )
            .addHandler(
                channel.handlerFor<StreamOffline> {
                    state(VOTHState.idFor(channel))?.pause() ?: error("No state for VOTH")
                }
            )
            .build()

        return SimpleFeatureRuntime(
            eventHandlers,
            id
        )
    }

    override fun getSpecificStates(stateProvider: StateProvider): List<State> {
        return listOf(VOTHState(channel, saveStatePath, stateProvider.getState(ClockState.ID)))
    }

    companion object {
        fun idFor(channel: TwitchChannel) = FeatureId("voth-${channel.name}")
    }
}