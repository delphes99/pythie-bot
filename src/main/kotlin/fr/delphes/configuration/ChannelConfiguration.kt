package fr.delphes.configuration

import fr.delphes.feature.Feature
import java.util.Properties

data class ChannelConfiguration(
    val ownerChannel: String,
    val ownerAccountOauth: String,
    val features: List<Feature>
) {
    constructor(
        ownerChannel: String,
        ownerAccountOauth: String,
        vararg features: Feature
    ) : this(ownerChannel, ownerAccountOauth, listOf(*features))

    companion object {
        fun build(
            path: String,
            buildConfiguration: (Properties) -> ChannelConfiguration
        ): ChannelConfiguration {
            return buildConfiguration(loadProperties(path))
        }
    }
}
