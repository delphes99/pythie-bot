package fr.delphes.feature

import fr.delphes.rework.feature.FeatureId

class FeatureConfigurationBuilderRegistry(
    val type: FeatureConfigurationType,
    val provideNewConfiguration: (FeatureId) -> FeatureConfiguration,
)