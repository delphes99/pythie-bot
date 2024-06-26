package fr.delphes.generation.outgoingEvent.generateRegistryProcessor

import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.ksp.writeTo
import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
import fr.delphes.bot.event.outgoing.OutgoingEventRegistry
import fr.delphes.bot.event.outgoing.OutgoingEventRegistryEntry
import fr.delphes.feature.OutgoingEventType
import fr.delphes.generation.outgoingEvent.generateBuilderProcessor.GenerateOutgoingEventBuilderModuleProcessor
import fr.delphes.generation.utils.GenerationUtils.baseGeneratedPackage
import fr.delphes.generation.utils.GenerationUtils.getModuleName

class GenerateOutgoingEventRegistryProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return GenerateOutgoingEventRegistryProcessor(
            environment.codeGenerator,
            environment.logger,
            getModuleName(environment),
        )
    }
}

class GenerateOutgoingEventRegistryProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val moduleName: String,
) : SymbolProcessor {
    private val allOutgoingEvents = mutableSetOf<KSClassDeclaration>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("Start processing : Generate Outgoing Event Registry")
        allOutgoingEvents.addAll(
            resolver.getSymbolsWithAnnotation(RegisterOutgoingEvent::class.java.name)
                .filterIsInstance<KSClassDeclaration>()
        )

        return emptyList()
    }

    override fun finish() {
        super.finish()

        FileSpec
            .builder(
                "${baseGeneratedPackage(moduleName)}.outgoingEvent",
                "${moduleName}OutgoingEventRegistry"
            )
            .addProperty(
                PropertySpec.builder(
                    "${moduleName}OutgoingEventRegistry",
                    OutgoingEventRegistry::class,
                    KModifier.INTERNAL
                )
                    .initializer(
                        CodeBlock.builder()
                            .addStatement("OutgoingEventRegistry(")
                            .addStatement("listOf(")
                            .apply {
                                allOutgoingEvents.forEach { event ->
                                    val serialName = event.getSerialName()
                                    val builderClassName =
                                        GenerateOutgoingEventBuilderModuleProcessor.builderClassName(moduleName, event)

                                    addStatement(
                                        "%T(",
                                        OutgoingEventRegistryEntry::class
                                    )
                                    addStatement(
                                        "%T(\"$serialName\"),",
                                        OutgoingEventType::class,
                                    )
                                    addStatement(") {")
                                    addStatement("%T()", builderClassName)
                                    addStatement("},")
                                }
                            }
                            .addStatement(")")
                            .addStatement(")")
                            .build()
                    )
                    .build()
            )
            .build()
            .writeTo(codeGenerator, false, getSources())
    }

    private fun getSources() = allOutgoingEvents.map { it.containingFile as KSFile }
}

private fun KSClassDeclaration.getSerialName() =
    getAnnotationsByType(RegisterOutgoingEvent::class)
        .first().serializeName