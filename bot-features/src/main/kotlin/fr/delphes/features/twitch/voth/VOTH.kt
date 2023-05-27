package fr.delphes.features.twitch.voth

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.command.LegacyCommandHandler
import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.connector.twitch.outgoingEvent.PromoteVIP
import fr.delphes.connector.twitch.outgoingEvent.RemoveVIP
import fr.delphes.connector.twitch.state.GetVipState
import fr.delphes.feature.HavePersistantState
import fr.delphes.feature.NonEditableFeature
import fr.delphes.feature.StateRepository
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import kotlinx.coroutines.runBlocking

class VOTH(
    override val channel: TwitchChannel,
    private val configuration: VOTHConfiguration,
    override val stateRepository: StateRepository<LegacyVOTHState>,
    override val state: LegacyVOTHState = runBlocking { stateRepository.load() },
    private val clock: Clock = SystemClock,
) : NonEditableFeature, TwitchFeature, HavePersistantState<LegacyVOTHState> {
    private val statsCommand = Command(configuration.statsCommand)
    private val top3Command = Command(configuration.top3Command)

    override val eventHandlers = LegacyEventHandlers
        .builder()
        .addHandler(VOTHRewardRedemptionHandler())
        .addHandler(StreamOnlineHandler())
        .addHandler(StreamOfflineHandler())
        .addHandler(buildCommandStatsHandler())
        .addHandler(buildCommandTop3Handler())
        .build()

    private fun buildCommandStatsHandler() = LegacyCommandHandler(
        channel,
        statsCommand
    ) { user, _ ->
        val stats = state.getReignsFor(user, clock.now())
        configuration.statsResponse(stats)
    }

    private fun buildCommandTop3Handler() = LegacyCommandHandler(
        channel,
        top3Command
    ) { _, _ ->
        val tops = state.top3(clock.now())
        configuration.top3Response(tops.getOrNull(0), tops.getOrNull(1), tops.getOrNull(2))
    }

    override val commands: Iterable<Command> = listOf(statsCommand, top3Command)

    internal val currentVip get() = state.currentVip

    internal inner class VOTHRewardRedemptionHandler : TwitchEventHandler<RewardRedemption>(channel) {
        override suspend fun handleIfGoodChannel(event: RewardRedemption, bot: Bot): List<OutgoingEvent> {
            val redeemUser = event.user
            return if (configuration.reward.rewardConfiguration.match(event.reward) && currentVip?.user != redeemUser) {
                val oldVOTH = currentVip
                val newVOTH = state.newVOTH(event, clock.now())
                save()

                val removeAllVips = bot.featuresManager.stateManager
                    .getStateOrNull(GetVipState.ID)
                    ?.getVipOf(channel)
                    ?.map { RemoveVIP(UserName(it.name), channel) }
                    ?: emptyList()

                val promoteVIP = PromoteVIP(redeemUser, channel)

                val alert = Alert("newVip", "newVip" to (currentVip!!.user.name))

                val newVipAnnouncer = configuration.newVipAnnouncer(
                    NewVOTHAnnounced(
                        oldVOTH,
                        newVOTH,
                        event
                    )
                )
                listOf(
                    *removeAllVips.toTypedArray(),
                    promoteVIP,
                    *newVipAnnouncer.toTypedArray(),
                    alert
                )
            } else {
                emptyList()
            }
        }
    }

    internal inner class StreamOnlineHandler : TwitchEventHandler<StreamOnline>(channel) {
        override suspend fun handleIfGoodChannel(event: StreamOnline, bot: Bot): List<OutgoingEvent> {
            state.unpause(clock.now())
            save()
            return emptyList()
        }
    }

    internal inner class StreamOfflineHandler : TwitchEventHandler<StreamOffline>(channel) {
        override suspend fun handleIfGoodChannel(event: StreamOffline, bot: Bot): List<OutgoingEvent> {
            state.pause(clock.now())
            save()
            return emptyList()
        }
    }
}