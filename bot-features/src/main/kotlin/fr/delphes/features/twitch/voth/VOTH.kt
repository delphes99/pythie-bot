package fr.delphes.features.twitch.voth

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.command.CommandHandler
import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.connector.twitch.incomingEvent.VIPListReceived
import fr.delphes.connector.twitch.outgoingEvent.PromoteVIP
import fr.delphes.connector.twitch.outgoingEvent.RemoveVIP
import fr.delphes.connector.twitch.outgoingEvent.RetrieveVip
import fr.delphes.feature.HavePersistantState
import fr.delphes.feature.NonEditableFeature
import fr.delphes.feature.StateRepository
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import kotlinx.coroutines.runBlocking

class VOTH(
    override val channel: TwitchChannel,
    private val configuration: VOTHConfiguration,
    override val stateRepository: StateRepository<VOTHState>,
    override val state: VOTHState = runBlocking { stateRepository.load() },
    private val clock: Clock = SystemClock
) : NonEditableFeature<VOTHDescription>, TwitchFeature, HavePersistantState<VOTHState> {
    override fun description() = VOTHDescription(
        channel.name,
        configuration.reward.rewardConfiguration.title,
        configuration.statsCommand,
        configuration.top3Command
    )

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(VOTHRewardRedemptionHandler())
        eventHandlers.addHandler(VOTHVIPListReceivedHandler())
        eventHandlers.addHandler(StreamOnlineHandler())
        eventHandlers.addHandler(StreamOfflineHandler())
        eventHandlers.addHandler(commandStatsHandler)
        eventHandlers.addHandler(commandTop3Handler)
    }

    private val commandStatsHandler = CommandHandler(
        channel,
        Command(configuration.statsCommand)
    ) { user, _ ->
        val stats = state.getReignsFor(user, clock.now())
        configuration.statsResponse(stats)
    }

    private val commandTop3Handler = CommandHandler(
        channel,
        Command(configuration.top3Command)
    ) { _, _ ->
        val tops = state.top3(clock.now())
        configuration.top3Response(tops.getOrNull(0), tops.getOrNull(1), tops.getOrNull(2))
    }

    override val commands: Iterable<Command> = listOf(commandStatsHandler.command, commandTop3Handler.command)

    internal val currentVip get() = state.currentVip
    internal val vothChanged get() = state.vothChanged

    internal inner class VOTHRewardRedemptionHandler : TwitchEventHandler<RewardRedemption>(channel) {
        override suspend fun handleIfGoodChannel(event: RewardRedemption, bot: Bot): List<OutgoingEvent> {
            val redeemUser = event.user
            return if (configuration.reward.rewardConfiguration.match(event.reward) && currentVip?.user != redeemUser) {
                val oldVOTH = currentVip
                val newVOTH = state.newVOTH(event, clock.now())

                configuration.newVipAnnouncer(
                    NewVOTHAnnounced(
                        oldVOTH,
                        newVOTH,
                        event
                    )
                ) + RetrieveVip(channel)
            } else {
                emptyList()
            }
        }
    }

    internal inner class VOTHVIPListReceivedHandler : TwitchEventHandler<VIPListReceived>(channel) {
        override suspend fun handleIfGoodChannel(event: VIPListReceived, bot: Bot): List<OutgoingEvent> {
            return if (vothChanged) {
                state.vothChanged = false

                val alert = Alert("newVip", "newVip" to (currentVip!!.user.name))

                val events =
                    event.vips.map { RemoveVIP(it, channel) } + PromoteVIP(currentVip!!.user, channel) + alert
                save()
                events
            } else {
                emptyList()
            }
        }
    }

    internal inner class StreamOnlineHandler : TwitchEventHandler<StreamOnline>(channel) {
        override suspend fun handleIfGoodChannel(event: StreamOnline, bot: Bot): List<OutgoingEvent> {
            state.unpause(clock.now())
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