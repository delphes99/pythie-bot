package fr.delphes.feature.voth

import fr.delphes.bot.command.Command
import fr.delphes.bot.command.CommandHandler
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.bot.event.incoming.StreamOffline
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.event.incoming.VIPListReceived
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.event.outgoing.PromoteVIP
import fr.delphes.bot.event.outgoing.RemoveVIP
import fr.delphes.bot.event.outgoing.RetrieveVip
import fr.delphes.bot.time.Clock
import fr.delphes.bot.time.SystemClock
import fr.delphes.feature.AbstractFeature
import fr.delphes.feature.HavePersistantState
import fr.delphes.feature.StateRepository

class VOTH(
    private val configuration: VOTHConfiguration,
    override val stateRepository: StateRepository<VOTHState>,
    override val state: VOTHState = stateRepository.load(),
    private val clock: Clock = SystemClock
) : AbstractFeature(), HavePersistantState<VOTHState> {
    private val commandStats = CommandHandler(
        configuration.statsCommand
    ) { user ->
        val stats = state.getReignsFor(user, clock.now())
        configuration.statsResponseEvents(stats)
    }

    override val commands: Iterable<Command> = listOf(commandStats)

    override val rewardHandlers: List<EventHandler<RewardRedemption>> = listOf(VOTHRewardRedemptionHandler())
    override val vipListReceivedHandlers: List<EventHandler<VIPListReceived>> = listOf(VOTHVIPListReceivedHandler())
    override val streamOnlineHandlers: List<EventHandler<StreamOnline>> = listOf(StreamOnlineHandler())
    override val streamOfflineHandlers: List<EventHandler<StreamOffline>> = listOf(StreamOfflineHandler())

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

    private inner class StreamOnlineHandler : EventHandler<StreamOnline> {
        override fun handle(event: StreamOnline): List<OutgoingEvent> {
            state.unpause(clock.now())
            return emptyList()
        }
    }

    private inner class StreamOfflineHandler : EventHandler<StreamOffline> {
        override fun handle(event: StreamOffline): List<OutgoingEvent> {
            state.pause(clock.now())
            save()
            return emptyList()
        }
    }
}