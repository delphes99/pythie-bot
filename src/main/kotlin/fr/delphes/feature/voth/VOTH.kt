package fr.delphes.feature.voth

import fr.delphes.event.eventHandler.EventHandler
import fr.delphes.event.incoming.MessageReceived
import fr.delphes.event.incoming.RewardRedemption
import fr.delphes.event.incoming.VIPListReceived
import fr.delphes.event.outgoing.OutgoingEvent
import fr.delphes.event.outgoing.PromoteVIP
import fr.delphes.event.outgoing.RemoveVIP
import fr.delphes.event.outgoing.RetrieveVip
import fr.delphes.feature.AbstractFeature
import fr.delphes.feature.HaveState
import fr.delphes.feature.StateRepository
import fr.delphes.time.Clock
import fr.delphes.time.SystemClock

class VOTH(
    private val configuration: VOTHConfiguration,
    override val stateRepository: StateRepository<VOTHState>,
    override val state: VOTHState = stateRepository.load(),
    private val clock: Clock = SystemClock
) : AbstractFeature(), HaveState<VOTHState> {
    override val rewardHandlers: List<EventHandler<RewardRedemption>> = listOf(VOTHRewardRedemptionHandler())
    override val vipListReceivedHandlers: List<EventHandler<VIPListReceived>> = listOf(VOTHVIPListReceivedHandler())
    override val messageReceivedHandlers: List<EventHandler<MessageReceived>> = listOf(CommandStats())

    val currentVip get() = state.currentVip
    val vothChanged get() = state.vothChanged

    private inner class VOTHRewardRedemptionHandler : EventHandler<RewardRedemption> {
        override fun handle(event: RewardRedemption): List<OutgoingEvent> {
            val redeemUser = event.user
            return if (event.rewardId == configuration.featureId && currentVip?.user != redeemUser) {
                val oldVOTH = currentVip
                val newVOTH = state.newVOTH(event, clock.now())

                configuration.newVipAnnouncer(
                    NewVOTHAnnounced(
                        oldVOTH,
                        newVOTH,
                        event
                    )
                ) + RetrieveVip
            } else {
                emptyList()
            }
        }
    }

    private inner class VOTHVIPListReceivedHandler : EventHandler<VIPListReceived> {
        override fun handle(event: VIPListReceived): List<OutgoingEvent> {
            return if (vothChanged) {
                state.vothChanged = false
                val events = event.vips.map(::RemoveVIP) + PromoteVIP(currentVip!!.user)
                save()
                events
            } else {
                emptyList()
            }
        }
    }

    private inner class CommandStats : EventHandler<MessageReceived> {
        override fun handle(event: MessageReceived): List<OutgoingEvent> {
            return if (event.text.toLowerCase() == configuration.statsCommand) {
                val stats = state.getReignsFor(event.user, clock.now())
                configuration.statsResponseEvents(stats)
            } else {
                emptyList()
            }
        }
    }
}