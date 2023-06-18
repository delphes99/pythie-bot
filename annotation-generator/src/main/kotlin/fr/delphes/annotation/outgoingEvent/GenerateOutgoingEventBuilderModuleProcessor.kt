package fr.delphes.annotation.outgoingEvent

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import fr.delphes.generation.GenerationUtils.getModuleName
import fr.delphes.generation.GenerationUtils.processEach

class GenerateOutgoingEventBuilderModuleProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return GenerateOutgoingEventBuilderModuleProcessor(
            environment.codeGenerator,
            environment.logger,
            getModuleName(environment),
        )
    }
}

class GenerateOutgoingEventBuilderModuleProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val moduleName: String,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        return resolver.processEach(logger, RegisterOutgoingEvent::class, ::create)
    }

    private fun create(outgoingEventClass: KSClassDeclaration) {
        checkInheritFromOutgoingEvent(outgoingEventClass)
        FileSpec.builder(
            "fr.delphes.$moduleName.generated.outgoingEvent",
            outgoingEventClass.simpleName.asString() + "Builder"
        )
            .build()
            .writeTo(codeGenerator, false)
    }

    private fun checkInheritFromOutgoingEvent(outgoingEventClass: KSClassDeclaration) {
        if (outgoingEventClass.superTypes.none { it.resolve().declaration.qualifiedName?.asString() == "fr.delphes.bot.event.outgoing.OutgoingEvent" }) {
            logger.error("${outgoingEventClass.qualifiedName?.asString()} must implement OutgoingEvent")
        }
    }
}
