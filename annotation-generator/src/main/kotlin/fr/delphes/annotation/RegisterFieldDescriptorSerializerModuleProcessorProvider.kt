package fr.delphes.annotation

import com.squareup.kotlinpoet.ClassName
import fr.delphes.generation.PolymorphicSerializerModuleProcessorProvider

class RegisterFieldDescriptorSerializerModuleProcessorProvider : PolymorphicSerializerModuleProcessorProvider(
    RegisterFieldDescriptor::class.java,
    ClassName("fr.delphes.feature.descriptor", "FeatureDescriptor"),
    "FieldDescriptorSerializerModule",
)