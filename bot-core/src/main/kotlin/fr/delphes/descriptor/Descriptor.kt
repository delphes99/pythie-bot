package fr.delphes.descriptor

import fr.delphes.descriptor.item.ItemDescriptor
import kotlin.reflect.KClass

@kotlinx.serialization.Serializable
data class Descriptor(
    val type: DescriptorType,
    val items: List<ItemDescriptor> = emptyList()
) {
    constructor(type: DescriptorType, vararg items: ItemDescriptor) : this(type, items.toList())
}

inline fun <reified U: KClass<*>> U.toDescriptor(vararg items: ItemDescriptor): Descriptor =
    this.toDescriptor(items.toList())

inline fun <reified U: KClass<*>> U.toDescriptor(items: List<ItemDescriptor>): Descriptor =
    Descriptor(this.toDescriptorType(), items)

inline fun <reified U: Any> U.toDescriptor(vararg items: ItemDescriptor): Descriptor =
    U::class.toDescriptor(items.toList())

inline fun <reified U: Any> U.toDescriptor(items: List<ItemDescriptor>, identifier: String? = null): Descriptor =
    U::class.toDescriptor(items)