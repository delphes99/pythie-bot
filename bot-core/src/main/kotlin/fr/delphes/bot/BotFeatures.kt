package fr.delphes.bot

import fr.delphes.feature.featureNew.FeatureConfiguration
import fr.delphes.feature.featureNew.FeatureIdentifier
import fr.delphes.feature.featureNew.FeatureType

class BotFeatures(
    private val features: Map<FeatureType, (FeatureIdentifier) -> FeatureConfiguration<*>>
) {
    fun build(id: FeatureIdentifier, type: FeatureType) : FeatureConfiguration<*>? {
        return features.filter { (key, _) -> key == type }.map { (_, value) -> value(id) }.firstOrNull()
    }
}