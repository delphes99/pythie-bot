package fr.delphes.feature

import fr.delphes.descriptor.registry.DescriptorBuilder
import fr.delphes.descriptor.registry.DescriptorRegistry
import fr.delphes.descriptor.toDescriptor
import fr.delphes.descriptor.toDescriptorType

internal object DescriptorTestUtil {
    class Type1
    class Type2

    val descriptorType1 = Type1::class.toDescriptorType()
    val descriptorType2 = Type2::class.toDescriptorType()

    val descriptor1 = Type1::class.toDescriptor()
    val descriptor2 = Type2::class.toDescriptor()

    val mapper1 = DescriptorBuilder.buildDescriptor<Type1> { _, _ -> descriptor1 }
    val mapper2 = DescriptorBuilder.buildDescriptor<Type2> { _, _ -> descriptor2 }

    val emptyDescriptorRegistry = DescriptorRegistry.of<Any>()
}