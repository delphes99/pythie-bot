package fr.delphes.annotation.serialization

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.ksp.writeTo
import fr.delphes.annotation.incomingEvent.incomingEventSerializerModule
import fr.delphes.annotation.outgoingEvent.registerBuilder.outgoingEventSerializerModule
import fr.delphes.generation.GenerationUtils.getModuleName
import kotlinx.serialization.modules.SerializersModule

val serializerModules = listOf(
    incomingEventSerializerModule,
    outgoingEventSerializerModule,
)

class PolymorphicSerializerModuleProcessorProvider(
) : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return PolymorphicSerializerModuleProcessor(
            environment.codeGenerator,
            environment.logger,
            serializerModules,
            getModuleName(environment),
        )
    }
}

class PolymorphicSerializerModuleProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val modules: List<SerializerModule>,
    private val moduleName: String,
) : SymbolProcessor {
    private val childrenByModule = mutableMapOf<SerializerModule, MutableSet<KSClassDeclaration>>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("Start processing : PolymorphicSerializerModuleProcessor")
        modules.forEach { module ->
            resolver.getSymbolsWithAnnotation(module.annotationClass.java.name)
                .filterIsInstance<KSClassDeclaration>()
                .forEach { declaration ->
                    childrenByModule
                        .getOrPut(module) { mutableSetOf() }
                        .add(declaration)
                }
        }

        return emptyList()
    }

    override fun finish() {
        super.finish()

        FileSpec
            .builder(
                "fr.delphes.$moduleName.generated",
                "${moduleName}PolymorphicSerializerModule"
            )
            .addProperty(
                PropertySpec.builder(
                    "${moduleName}PolymorphicSerializerModule",
                    SerializersModule::class,
                    KModifier.INTERNAL
                )
                    .initializer(
                        CodeBlock.builder()
                            .addStatement("SerializersModule {")
                            .apply {
                                childrenByModule.forEach { (module, children) ->
                                    toModuleCode(module, children)
                                }
                            }
                            .addStatement("}")
                            .build()
                    )
                    .build()
            )
            .build()
            .writeTo(codeGenerator, true, getSources())
    }

    private fun getSources() = childrenByModule.values.flatten().map { it.containingFile as KSFile }

    private fun CodeBlock.Builder.toModuleCode(
        module: SerializerModule,
        children: MutableSet<KSClassDeclaration>,
    ) {
        addStatement("%T(%T::class) {", POLYMORPHIC, module.parentClassName)
        children.forEach {
            addStatement("%T(%T::class)", SUBCLASS, it.toClassName())
        }
        addStatement("}")
    }

    private fun KSClassDeclaration.toClassName() =
        ClassName(packageName.asString(), simpleName.asString())

    companion object {
        private val POLYMORPHIC = ClassName("kotlinx.serialization.modules", "polymorphic")
        private val SUBCLASS = ClassName("kotlinx.serialization.modules", "subclass")
    }
}

