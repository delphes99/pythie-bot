package fr.delphes.feature.voth

import fr.delphes.event.eventHandler.EventHandler
import fr.delphes.event.incoming.MessageReceived
import fr.delphes.event.incoming.RewardRedemption
import fr.delphes.event.incoming.VIPListReceived
import fr.delphes.event.outgoing.OutgoingEvent
import fr.delphes.event.outgoing.PromoteVIP
import fr.delphes.event.outgoing.RemoveVIP
import fr.delphes.event.outgoing.RetrieveVip
import fr.delphes.event.outgoing.SendMessage
import fr.delphes.feature.AbstractFeature
import fr.delphes.feature.HaveState
import fr.delphes.feature.StateManager
import fr.delphes.time.Clock
import fr.delphes.time.SystemClock
import fr.delphes.time.prettyPrint

class VOTH(
    private val feature: String,
    private val clock: Clock = SystemClock,
    override val state: VOTHState = VOTHState(),
    override val stateManager: StateManager<VOTHState>,
    private val newVipAnnouncer: ((NewVOTHAnnounced) -> String)?
) : AbstractFeature(), HaveState<VOTHState> {
    override val rewardHandlers: List<EventHandler<RewardRedemption>> = listOf(VOTHRewardRedemptionHandler())
    override val vipListReceivedHandlers: List<EventHandler<VIPListReceived>> = listOf(VOTHVIPListReceivedHandler())
    override val messageReceivedHandlers: List<EventHandler<MessageReceived>> = listOf(CommandStats())

    val currentVip get() = state.currentVip
    val vothChanged get() = state.vothChanged

    private inner class VOTHRewardRedemptionHandler : EventHandler<RewardRedemption> {
        override fun handle(event: RewardRedemption): List<OutgoingEvent> {
            val redeemUser = event.user
            return if (event.rewardId == feature && currentVip?.user != redeemUser) {
                val oldVOTH = currentVip
                val newVOTH = state.newVOTH(event, clock.now())

                listOfNotNull(
                    newVipAnnouncer?.let { announcer ->
                        SendMessage(
                            announcer(
                                NewVOTHAnnounced(
                                    oldVOTH,
                                    newVOTH,
                                    event
                                )
                            )
                        )
                    },
                    RetrieveVip
                )
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
            return if (event.text.toLowerCase() == "!vothstats") {
                val stats = state.getReignsFor(event.user, clock.now())
                listOf(
                    SendMessage(
                        "Temps passé en tant que VOTH : ${stats.totalTime.prettyPrint()} --- " +
                                "Nombre de victoires : ${stats.numberOfReigns} --- " +
                                "Total de points dépensés : ${stats.totalCost}"
                    )
                )
            } else {
                emptyList()
            }
        }
    }
}