package fr.delphes.features.twitch

import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.StateManager
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid
import kotlin.reflect.KClass

abstract class SimpleTwitchEventFeature<EVENT : TwitchIncomingEvent>(
    private val eventClass: KClass<EVENT>,
    override val id: FeatureId = FeatureId(uuid()),
    private val action: suspend TwitchEventParameters<EVENT>.() -> Unit
    //TODO merge TwitchFeature and FeatureDefinition
) : TwitchFeature, FeatureDefinition {
    abstract override val channel: TwitchChannel

    override fun buildRuntime(stateManager: StateManager): FeatureRuntime {
        val eventHandlers = channel.handlerFor(eventClass, action)

        return SimpleFeatureRuntime(eventHandlers, id)
    }
}
