package fr.delphes.features.twitch.streamerHighlight

import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.connector.twitch.outgoingEvent.ShoutOut
import fr.delphes.connector.twitch.state.GetUserInfos
import fr.delphes.connector.twitch.user.UserInfos
import fr.delphes.features.twitch.handlerFor
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.state.state.ClockState
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import java.time.Duration

class StreamerHighlightFeature(
    override val channel: TwitchChannel,
    private val highlightExpiration: Duration,
    private val activeStreamer: Duration,
    excludedUserNames: List<UserName> = emptyList(),
    override val id: FeatureId = FeatureId(),
    private val shoutOut: (MessageReceived, UserInfos) -> ShoutOut?,
) : TwitchFeature, FeatureDefinition {
    private val normalizedExcludedUserNames = excludedUserNames.map(UserName::normalizeName) + channel.normalizeName

    //TODO shared state with IncomingRaid : User defined state
    private val stateId = StreamerHighlightState.idFor(channel)

    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime {
        val eventHandlers = channel.handlerFor<MessageReceived> {
            val user = event.user
            if (normalizedExcludedUserNames.contains(user.normalizeName)) {
                return@handlerFor
            }

            val userInfos = stateManager.getState(GetUserInfos.ID).getUserInfos(user) ?: return@handlerFor
            val now = stateManager.getState(ClockState.ID).getValue()
            val highlightState = stateManager.getState(stateId)

            if (userInfos.isStreamer()
                && userInfos.hasStreamedSince(now.minus(activeStreamer))
                && !highlightState.isAlreadyHighlighted(user) { cacheTime ->
                    cacheTime.plus(highlightExpiration).isAfter(now)
                }
            ) {
                highlightState.highlight(user, now)
                shoutOut(event, userInfos)?.also {
                    executeOutgoingEvent(it)
                }
            }
        }

        return SimpleFeatureRuntime(eventHandlers, id)
    }

    override fun getSpecificStates(stateProvider: StateProvider): List<State> {
        return listOf(
            StreamerHighlightState(channel)
        )
    }
}