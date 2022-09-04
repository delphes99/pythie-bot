package fr.delphes.descriptor.registry

import fr.delphes.descriptor.Descriptor
import fr.delphes.descriptor.DescriptorType
import fr.delphes.descriptor.toDescriptorType

class DescriptorRegistry(
    entries: List<DescriptorRegistryEntry>
) : ToDescriptorMapper {
    @PublishedApi
    internal val map = entries.associateBy { it.type }

    fun getEntry(type: DescriptorType): DescriptorRegistryEntry? {
        return map[type]
    }

    override fun descriptorOf(configuration: Any, globalMapper: ToDescriptorMapper): Descriptor? {
        val mapper = map[configuration::class.toDescriptorType()]?.mapper as DescriptorBuilder<Any>?
        return mapper?.descriptorOf(configuration, globalMapper)
    }

    fun getRegisteredTypes(): List<DescriptorType> {
        return map.keys.toList()
    }

    companion object {
        fun <U : Any> of(outgoingMappers: List<DescriptorBuilder<out U>>): DescriptorRegistry {
            return DescriptorRegistry(outgoingMappers.map { mapper ->
                DescriptorRegistryEntry(
                    mapper.clazz.toDescriptorType(),
                    mapper
                )
            })
        }

        fun <U : Any> of(vararg outgoingMappers: DescriptorBuilder<out U>) = of(outgoingMappers.toList())
    }
}