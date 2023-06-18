package fr.delphes.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.ksp.writeTo
import fr.delphes.generation.GenerationUtils.getModuleName
import fr.delphes.generation.GenerationUtils.process
import kotlinx.serialization.modules.SerializersModule
import kotlin.reflect.KClass

abstract class PolymorphicSerializerModuleProcessorProvider(
    private val annotationClass: KClass<out Any>,
    private val parentClassName: ClassName,
    private val serializerModuleName: String,
) : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return PolymorphicSerializerModuleProcessor(
            environment.codeGenerator,
            environment.logger,
            annotationClass,
            parentClassName,
            serializerModuleName,
            getModuleName(environment),
        )
    }
}

class PolymorphicSerializerModuleProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val annotationClass: KClass<out Any>,
    private val parentClassName: ClassName,
    private val serializerModuleName: String,
    private val moduleName: String,
) : SymbolProcessor {
    private val propertyName get() = "$moduleName$serializerModuleName"

    override fun process(resolver: Resolver): List<KSAnnotated> {
        return resolver.process(logger, annotationClass, ::createSerializerModule)
    }

    private fun createSerializerModule(fieldDescriptorClasses: List<KSClassDeclaration>) {
        FileSpec.builder(
            "fr.delphes.$moduleName.generated",
            propertyName
        )
            .addProperty(fieldDescriptorClasses.toModuleVariable())
            .build()
            .writeTo(codeGenerator, false)
    }

    private fun List<KSClassDeclaration>.toModuleVariable() =
        PropertySpec.builder(propertyName, SerializersModule::class, KModifier.INTERNAL)
            .initializer(
                CodeBlock.builder()
                    .addStatement("SerializersModule {")
                    .addStatement("%T(%T::class) {", POLYMORPHIC, parentClassName)
                    .apply {
                        forEach {
                            addStatement("%T(%T::class)", SUBCLASS, it.toClassName())
                        }
                    }
                    .addStatement("}")
                    .addStatement("}")
                    .build()
            )
            .build()

    private fun KSClassDeclaration.toClassName() =
        ClassName(packageName.asString(), simpleName.asString())

    companion object {
        private val POLYMORPHIC = ClassName("kotlinx.serialization.modules", "polymorphic")
        private val SUBCLASS = ClassName("kotlinx.serialization.modules", "subclass")
    }
}

