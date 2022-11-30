package fr.delphes.feature

import fr.delphes.descriptor.registry.DescriptorBuilder
import fr.delphes.descriptor.toDescriptor

val personaFeatureDescriptor = PersonaFeatureConfiguration::class.toDescriptor()

val personaFeatureMapper = DescriptorBuilder.buildDescriptor<PersonaFeatureConfiguration> { _, _ -> personaFeatureDescriptor }