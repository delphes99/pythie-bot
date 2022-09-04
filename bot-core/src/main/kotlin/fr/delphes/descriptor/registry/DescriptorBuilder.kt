package fr.delphes.descriptor.registry

import fr.delphes.descriptor.Descriptor
import fr.delphes.descriptor.item.ItemDescriptor
import fr.delphes.descriptor.toDescriptor
import kotlin.reflect.KClass

class DescriptorBuilder<T : Any>(
    val clazz: KClass<T>,
    private val mapper: (T, ToDescriptorMapper) -> Descriptor,
) {
    fun descriptorOf(descriptible: T, globalMapper: ToDescriptorMapper): Descriptor {
        return mapper(descriptible, globalMapper)
    }

    companion object {
        inline fun <reified T : Any> buildDescriptor(
            noinline mapper: (T, ToDescriptorMapper) -> Descriptor
        ): DescriptorBuilder<T> {
            return DescriptorBuilder(T::class, mapper)
        }

        inline fun <reified T : Any> withItem(
            noinline mapper: (T, ToDescriptorMapper) -> List<ItemDescriptor>
        ): DescriptorBuilder<T> {
            return buildDescriptor { item, globalMapper -> item.toDescriptor(mapper(item, globalMapper)) }
        }
    }
}