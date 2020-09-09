package fr.delphes.feature.voth

import fr.delphes.event.eventHandler.EventHandler
import fr.delphes.event.incoming.RewardRedemption
import fr.delphes.event.incoming.VIPListReceived
import fr.delphes.event.outgoing.OutgoingEvent
import fr.delphes.event.outgoing.PromoteVIP
import fr.delphes.event.outgoing.RemoveVIP
import fr.delphes.event.outgoing.RetrieveVip
import fr.delphes.event.outgoing.SendMessage
import fr.delphes.feature.AbstractFeature
import fr.delphes.time.Clock
import fr.delphes.time.SystemClock

class VOTH(
    private val feature: String,
    private val clock: Clock = SystemClock,
    private val newVipAnnouncer: ((NewVOTHAnnounced) -> String)?
) : AbstractFeature() {
    override val rewardHandlers: List<EventHandler<RewardRedemption>> = listOf(VOTHRewardRedemptionHandler())
    override val vipListReceivedHandlers: List<EventHandler<VIPListReceived>> = listOf(VOTHVIPListReceivedHandler())

    internal var vothChanged = false
    internal var currentVip: VOTHWinner? = null

    private inner class VOTHRewardRedemptionHandler : EventHandler<RewardRedemption> {
        override fun handle(event: RewardRedemption): List<OutgoingEvent> {
            val redeemUser = event.displayName
            return if (event.rewardId == feature && currentVip?.user != redeemUser) {
                val oldVOTH = currentVip
                val newVOTH = VOTHWinner(redeemUser, clock.now())
                currentVip = newVOTH
                vothChanged = true

                listOfNotNull(
                    newVipAnnouncer?.let { announcer -> SendMessage(announcer(NewVOTHAnnounced(oldVOTH, newVOTH, event))) },
                    RetrieveVip
                )
            } else {
                emptyList()
            }
        }
    }

    private inner class VOTHVIPListReceivedHandler : EventHandler<VIPListReceived> {
        override fun handle(event: VIPListReceived): List<OutgoingEvent> {
            return if(vothChanged) {
                vothChanged = false
                event.vips.map(::RemoveVIP) + PromoteVIP(currentVip!!.user)
            } else {
                emptyList()
            }
        }
    }
}