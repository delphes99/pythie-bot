package fr.delphes.feature

import fr.delphes.descriptor.Descriptor
import fr.delphes.descriptor.registry.DescriptorRegistry
import fr.delphes.descriptor.registry.ToDescriptorMapper

class FeaturesManager(
    private val repository: ExperimentalFeatureConfigurationRepository,
    private val registry: DescriptorRegistry,
    private val globalRegistry: ToDescriptorMapper
) {
    suspend fun getDescriptors(): List<Descriptor> {
        return repository.getAll().mapNotNull { registry.descriptorOf(it, globalRegistry) }
    }
}