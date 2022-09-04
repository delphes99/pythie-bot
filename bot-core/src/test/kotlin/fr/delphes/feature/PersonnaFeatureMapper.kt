package fr.delphes.feature

import fr.delphes.descriptor.registry.DescriptorBuilder
import fr.delphes.descriptor.toDescriptor


val personnaFeatureDescriptor = PersonnaFeatureConfiguration::class.toDescriptor()

val personnaFeatureMapper = DescriptorBuilder.buildDescriptor<PersonnaFeatureConfiguration> { _, _ -> personnaFeatureDescriptor }