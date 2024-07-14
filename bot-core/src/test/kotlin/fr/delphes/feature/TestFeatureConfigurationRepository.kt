package fr.delphes.feature

import fr.delphes.rework.feature.Feature

class TestFeatureConfigurationRepository(
    val features: List<Feature>,
) : FeatureConfigurationRepository {
    constructor(vararg features: Feature) : this(features.toList())

    override suspend fun save(item: List<Feature>) {
        throw NotImplementedError()
    }

    override suspend fun upsert(configuration: Feature) {
        throw NotImplementedError()
    }

    override suspend fun load(): List<Feature> {
        return features
    }

}