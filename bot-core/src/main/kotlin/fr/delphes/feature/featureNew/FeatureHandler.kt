package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent

class FeatureHandler(
    private val _features: MutableMap<FeatureIdentifier, Feature> = mutableMapOf()
) {
    constructor(vararg configurations: FeatureConfiguration) : this(
        mutableMapOf(*configurations.map { it.identifier to Feature(it, it.buildRuntime()) }.toTypedArray())
    )

    val features: Map<FeatureIdentifier, Feature> get() = _features

    val configurations: Collection<FeatureConfiguration> get() = _features.values.map(Feature::configuration)
    val runtimes: Collection<FeatureRuntime> get() = _features.values.map(Feature::runtime)

    fun load(configurations: List<FeatureConfiguration>) {
        val newRuntimes = configurations.associate { configuration ->
            configuration.identifier to Feature(
                configuration,
                configuration.buildRuntime()
            )
        }

        _features.putAll(newRuntimes)
    }

    fun handleIncomingEvent(incomingEvent: IncomingEvent): List<OutgoingEvent> {
        return runtimes.flatMap { runtime -> runtime.execute(incomingEvent) }
    }

    class Feature(
        val configuration: FeatureConfiguration,
        val runtime: FeatureRuntime
    )
}