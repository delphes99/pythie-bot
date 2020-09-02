package fr.delphes.feature

import fr.delphes.event.eventHandler.EventHandler
import fr.delphes.event.incoming.RewardRedemption
import fr.delphes.event.incoming.VIPListReceived
import fr.delphes.event.outgoing.OutgoingEvent
import fr.delphes.event.outgoing.PromoteVIP
import fr.delphes.event.outgoing.RemoveVIP
import fr.delphes.event.outgoing.RetrieveVip
import fr.delphes.event.outgoing.SendMessage

class VOTH(
    val feature: String,
    val newVipAnnouncer: ((RewardRedemption) -> String)?
) : AbstractFeature() {
    override val rewardHandlers = listOf(VOTHRewardRedemptionHandler())
    override val vipListReceivedHandlers = listOf(VOTHVIPListReceivedHandler())

    private var currentVip = ""

    inner class VOTHRewardRedemptionHandler : EventHandler<RewardRedemption> {
        override fun handle(event: RewardRedemption): List<OutgoingEvent> {
            return if (event.rewardId == feature) {
                currentVip = event.displayName

                listOfNotNull(
                    newVipAnnouncer?.let { announcer -> SendMessage(announcer(event)) },
                    RetrieveVip
                )
            } else {
                emptyList()
            }
        }
    }

    inner class VOTHVIPListReceivedHandler : EventHandler<VIPListReceived> {
        override fun handle(event: VIPListReceived): List<OutgoingEvent> =
            event.vips.map(::RemoveVIP) + PromoteVIP(currentVip)
    }
}