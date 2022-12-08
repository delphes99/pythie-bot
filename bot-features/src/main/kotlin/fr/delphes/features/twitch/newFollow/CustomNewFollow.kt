package fr.delphes.features.twitch.newFollow

import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.connector.twitch.incomingEvent.NewFollow
import fr.delphes.rework.feature.CustomFeature
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.StateManager
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomNewFollow(
    val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    private val action: suspend CustomNewFollowParameters.() -> Unit
) : CustomFeature {
    override fun buildRuntime(stateManager: StateManager): FeatureRuntime {
        val eventHandlers = EventHandlers.of { event: NewFollow, bot ->
            if(event.channel == channel) {
                CustomNewFollowParameters(bot, event).action()
            }
        }

        return SimpleFeatureRuntime(eventHandlers, id)
    }
}

