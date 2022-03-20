package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent

class FeatureHandler(
    private val _features: MutableMap<FeatureIdentifier, Feature<out FeatureState>> = mutableMapOf()
) {
    constructor(vararg configurations: FeatureConfiguration<out FeatureState>) : this(
        mutableMapOf(*configurations.map { it.identifier to it.toFeature() }.toTypedArray())
    )

    val features: Map<FeatureIdentifier, Feature<out FeatureState>> get() = _features

    val configurations: Collection<FeatureConfiguration<out FeatureState>> get() = _features.values.map(Feature<*>::configuration)
    val runtimes: Collection<FeatureRuntime<out FeatureState>> get() = _features.values.map(Feature<*>::runtime)

    fun load(configurations: List<FeatureConfiguration<out FeatureState>>) {
        val newRuntimes = configurations.associate { configuration ->
            configuration.identifier to configuration.toFeature()
        }

        _features.putAll(newRuntimes)
    }

    fun handleIncomingEvent(incomingEvent: IncomingEvent): List<OutgoingEvent> {
        return runtimes.flatMap { runtime -> runtime.execute(incomingEvent) }
    }
}