package fr.delphes.descriptor.registry

import fr.delphes.descriptor.DescriptorType
import fr.delphes.descriptor.toDescriptorType

data class DescriptorRegistryEntry(
    val type: DescriptorType,
    val mapper: DescriptorBuilder<*>
) {
    companion object {
        inline fun <reified T : Any> of(mapper: DescriptorBuilder<T>) = DescriptorRegistryEntry(
            T::class.toDescriptorType(),
            mapper
        )
    }
}