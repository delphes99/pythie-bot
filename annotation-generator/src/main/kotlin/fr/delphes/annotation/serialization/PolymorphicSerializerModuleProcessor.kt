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
import fr.delphes.annotation.dynamicForm.RegisterDynamicFormDto
import fr.delphes.annotation.incomingEvent.RegisterIncomingEvent
import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEventBuilder
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.dynamicForm.DynamicFormDTO
import fr.delphes.generation.utils.GenerationUtils
import fr.delphes.generation.utils.GenerationUtils.getModuleName
import kotlinx.serialization.modules.SerializersModule

private val serializerModules = listOf(
    RegisterPolymorphic(
        RegisterIncomingEvent::class,
        IncomingEvent::class,
    ),
    RegisterPolymorphic(
        RegisterOutgoingEventBuilder::class,
        OutgoingEventBuilder::class,
    ),
    RegisterPolymorphic(
        RegisterDynamicFormDto::class,
        DynamicFormDTO::class,
    ),
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
    private val modules: List<RegisterPolymorphic>,
    private val moduleName: String,
) : SymbolProcessor {
    private val childrenByModule = mutableMapOf<RegisterPolymorphic, MutableSet<KSClassDeclaration>>()

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
                GenerationUtils.baseGeneratedPackage(moduleName),
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
        module: RegisterPolymorphic,
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

