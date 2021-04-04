package fr.delphes.features.twitch.streamerHighlight

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.NonEditableTwitchFeature
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.connector.twitch.user.UserInfos
import fr.delphes.feature.HavePersistantState
import fr.delphes.feature.StateRepository
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import kotlinx.coroutines.runBlocking
import java.time.Duration

class StreamerHighlightFeature(
    channel: TwitchChannel,
    private val highlightExpiration: Duration,
    private val response: (MessageReceived, UserInfos) -> List<OutgoingEvent>,
    override val stateRepository: StateRepository<StreamerHighlightState>,
    override val state: StreamerHighlightState = runBlocking { stateRepository.load() },
    private val clock: Clock = SystemClock,
) : NonEditableTwitchFeature<StreamerHighlightDescription>(channel), HavePersistantState<StreamerHighlightState> {
    override fun description() = StreamerHighlightDescription(channel.name)

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(MessageReceivedHandler())
    }

    inner class MessageReceivedHandler : TwitchEventHandler<MessageReceived>(channel) {
        override suspend fun handleIfGoodChannel(event: MessageReceived, bot: Bot): List<OutgoingEvent> {
            return bot.connector<TwitchConnector>()!!.whenRunning(
                whenRunning = {
                    val user = event.user
                    val userInfos = this.clientBot.userCache.getUser(user)
                    if (userInfos.isStreamer() && !user.isHighlighted() && event.channel.toUser() != user) {
                        highlight(user)
                        response(event, userInfos)
                    } else {
                        emptyList()
                    }
                },
                whenNotRunning = {
                    emptyList()
                }
            )
        }

        private fun User.isHighlighted() =
            state.isAlreadyHighlighted(this) { cacheTime ->
                cacheTime.plus(highlightExpiration).isAfter(clock.now())
            }
    }

    private suspend fun highlight(user: User) {
        state.highlight(user, clock.now())
        save()
    }
}