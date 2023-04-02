package fr.delphes.features.twitch.streamerHighlight

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.connector.twitch.outgoingEvent.ShoutOut
import fr.delphes.connector.twitch.user.UserInfos
import fr.delphes.feature.NonEditableFeature
import fr.delphes.feature.StateManagerWithRepository
import fr.delphes.feature.WithStateManager
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import java.time.Duration

class StreamerHighlightFeature(
    override val channel: TwitchChannel,
    private val highlightExpiration: Duration,
    private val activeStreamer: Duration,
    excludedUserNames: List<String>,
    private val shoutOut: (MessageReceived, UserInfos) -> ShoutOut?,
    stateManagerWithRepository: StateManagerWithRepository<StreamerHighlightState>,
    private val clock: Clock = SystemClock
) : NonEditableFeature,
    TwitchFeature,
    WithStateManager<StreamerHighlightState> by stateManagerWithRepository {
    override val eventHandlers = LegacyEventHandlers
        .builder()
        .addHandler(MessageReceivedHandler())
        .build()

    private val normalizedExcludedUserNames = excludedUserNames.map(String::lowercase) + channel.normalizeName

    inner class MessageReceivedHandler : TwitchEventHandler<MessageReceived>(channel) {
        override suspend fun handleIfGoodChannel(event: MessageReceived, bot: Bot): List<OutgoingEvent> {
            val user = event.user
            if(normalizedExcludedUserNames.contains(user.normalizeName)) {
                return emptyList()
            }

            val userInfos = bot.connector<TwitchConnector>()!!.getUser(user)

            return if (userInfos != null
                && userInfos.isStreamer()
                && userInfos.hasStreamedSince(clock.now().minus(activeStreamer))
                && !user.isHighlighted()
            ) {
                highlight(user)
                listOfNotNull(shoutOut(event, userInfos))
            } else {
                emptyList()
            }
        }

        private fun UserName.isHighlighted() =
            state.isAlreadyHighlighted(this) { cacheTime ->
                cacheTime.plus(highlightExpiration).isAfter(clock.now())
            }
    }

    private suspend fun highlight(user: UserName) {
        newState(state.highlight(user, clock.now()))
    }
}