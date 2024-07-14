package fr.delphes.feature

import fr.delphes.rework.feature.Feature

interface FeatureConfigurationRepository {
    suspend fun save(item: List<Feature>)

    suspend fun upsert(configuration: Feature)

    suspend fun load(): List<Feature>
}