package fr.delphes.descriptor.registry

import fr.delphes.descriptor.Descriptor

class MergeDescriptorRegistry(
    private val registries: List<DescriptorRegistry>
) : ToDescriptorMapper {
    constructor(vararg registries: DescriptorRegistry) : this(registries.toList())

    override fun descriptorOf(value: Any, globalMapper: ToDescriptorMapper): Descriptor? {
        return registries.firstNotNullOfOrNull { registry -> registry.descriptorOf(value, globalMapper) }
    }
}