package fr.delphes.features.twitch.streamerHighlight

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.feature.Feature
import fr.delphes.feature.HavePersistantState
import fr.delphes.feature.StateRepository
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import kotlinx.coroutines.runBlocking

class StreamerHighlightFeature(
    private val channel: TwitchChannel,
    private val response: (MessageReceived) -> List<OutgoingEvent>,
    override val stateRepository: StateRepository<StreamerHighlightState>,
    override val state: StreamerHighlightState = runBlocking { stateRepository.load() },
    private val clock: Clock = SystemClock,
) : Feature, HavePersistantState<StreamerHighlightState> {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(MessageReceivedHandler())
    }

    inner class MessageReceivedHandler : TwitchEventHandler<MessageReceived>(channel) {
        override suspend fun handleIfGoodChannel(event: MessageReceived, bot: Bot): List<OutgoingEvent> {
            return bot.connector<TwitchConnector>()!!.whenRunning(
                whenRunning = {
                    val user = event.user
                    val userInfos = this.clientBot.userCache.getUser(user)
                    if (userInfos.isStreamer() && !state.isAlreadyHighlighted(user)) {
                        highlight(user)
                        response(event)
                    } else {
                        emptyList()
                    }
                },
                whenNotRunning = {
                    emptyList()
                }
            )
        }
    }

    private suspend fun highlight(user: User) {
        state.highlight(user, clock.now())
        save()
    }
}