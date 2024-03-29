package fr.delphes.features.twitch

import fr.delphes.bot.event.eventHandler.IncomingEventHandlerAction
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid
import kotlin.reflect.KClass

abstract class SimpleTwitchEventFeature<EVENT : TwitchIncomingEvent>(
    private val eventClass: KClass<EVENT>,
    override val id: FeatureId = FeatureId(uuid()),
    private val action: IncomingEventHandlerAction<EVENT>,
    //TODO merge TwitchFeature and FeatureDefinition
) : TwitchFeature, FeatureDefinition {
    abstract override val channel: TwitchChannel

    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime {
        val eventHandlers = channel.handlersFor(eventClass, action)

        return SimpleFeatureRuntime(eventHandlers, id)
    }
}
