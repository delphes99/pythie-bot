package fr.delphes.descriptor.registry

import fr.delphes.descriptor.Descriptor

interface ToDescriptorMapper {
    fun descriptorOf(configuration: Any, globalMapper: ToDescriptorMapper): Descriptor?
}