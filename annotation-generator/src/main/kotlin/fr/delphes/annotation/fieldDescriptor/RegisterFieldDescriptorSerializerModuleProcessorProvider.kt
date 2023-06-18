package fr.delphes.annotation.fieldDescriptor

import com.squareup.kotlinpoet.ClassName
import fr.delphes.generation.PolymorphicSerializerModuleProcessorProvider

class RegisterFieldDescriptorSerializerModuleProcessorProvider : PolymorphicSerializerModuleProcessorProvider(
    RegisterFieldDescriptor::class,
    ClassName("fr.delphes.feature.descriptor", "FeatureDescriptor"),
    "FieldDescriptorSerializerModule",
)