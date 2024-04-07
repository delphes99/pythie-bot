package fr.delphes.generation.dynamicForm.generateRegistryProcessor

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
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.dynamicForm.DynamicFormRegistry
import fr.delphes.dynamicForm.DynamicFormRegistryEntry
import fr.delphes.generation.utils.GenerationUtils

class GenerateRegistryProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return GenerateRegistryProcessor(
            environment.codeGenerator,
            environment.logger,
            GenerationUtils.getModuleName(environment),
        )
    }

    class GenerateRegistryProcessor(
        private val codeGenerator: CodeGenerator,
        private val logger: KSPLogger,
        private val moduleName: String,
    ) : SymbolProcessor {
        private val allForms = mutableSetOf<KSClassDeclaration>()

        override fun process(resolver: Resolver): List<KSAnnotated> {
            logger.info("Start processing : Generate Dynamic Forms Registry")
            allForms.addAll(
                resolver.getSymbolsWithAnnotation(DynamicForm::class.java.name)
                    .filterIsInstance<KSClassDeclaration>()
            )

            return emptyList()
        }

        override fun finish() {
            super.finish()

            FileSpec
                .builder(
                    "${GenerationUtils.baseGeneratedPackage(moduleName)}.dynamicForm",
                    "${moduleName}DynamicFormRegistry"
                )
                .addProperty(
                    PropertySpec.builder(
                        "${moduleName}DynamicFormRegistry",
                        DynamicFormRegistry::class,
                        KModifier.PUBLIC
                    )
                        .initializer(
                            CodeBlock.builder()
                                .addStatement("DynamicFormRegistry.of(")
                                .addStatement("listOf(")
                                .apply {
                                    allForms.forEach { form ->
                                        val dynamicFormAnnotation =
                                            form.getAnnotationsByType(DynamicForm::class).first()

                                        addStatement("%T.of(", DynamicFormRegistryEntry::class)
                                        addStatement("%S, ", dynamicFormAnnotation.name)
                                        addStatement("%T::class, ", form.toClassName())
                                        addStatement("listOf(")
                                        val tags = dynamicFormAnnotation.tags
                                            .joinToString(",") { "\"$it\"" }
                                        addStatement(tags)
                                        addStatement(")")
                                        addStatement("),")
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

        private fun getSources() = allForms.map { it.containingFile as KSFile }
    }
}
