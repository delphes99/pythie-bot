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
import com.google.devtools.ksp.symbol.KSName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.DynamicFormParent
import fr.delphes.dynamicForm.DynamicFormRegistry
import fr.delphes.dynamicForm.DynamicFormRegistryEntry
import fr.delphes.dynamicForm.DynamicFormType
import fr.delphes.generation.dynamicForm.generateFormProcessor.GenerateDynamicFormProcessor
import fr.delphes.generation.dynamicForm.metadata.MetadataExtractor
import fr.delphes.generation.outgoingEvent.generateBuilderProcessor.FieldDescriptionFactory
import fr.delphes.generation.utils.GenerationUtils
import fr.delphes.generation.utils.getAllAnnotations
import fr.delphes.generation.utils.getSources

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
        private lateinit var lastResolver: Resolver
        private val allFormNames = mutableSetOf<KSName>()

        override fun process(resolver: Resolver): List<KSAnnotated> {
            lastResolver = resolver
            logger.info("Start processing : Generate Dynamic Forms Registry")
            allFormNames.addAll(
                resolver.getSymbolsWithAnnotation(DynamicForm::class.java.name)
                    .filterIsInstance<KSClassDeclaration>()
                    .map { it.qualifiedName!! }
            )

            return emptyList()
        }

        override fun finish() {
            super.finish()
            val allForms = allFormNames.map { formClassName ->
                lastResolver.getClassDeclarationByName(formClassName)!!
            }
            val metadataExtractor = MetadataExtractor()
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

                                        addStatement("%T.of(", DynamicFormRegistryEntry::class).apply {
                                            addStatement(
                                                "%T(%S), ",
                                                DynamicFormType::class,
                                                dynamicFormAnnotation.name
                                            )
                                            addStatement("%T::class, ", form.toClassName())
                                            addStatement("listOf(").apply {
                                                val tags =
                                                    form.getAllAnnotations(DynamicFormParent::class)
                                                        .map { it.family }
                                                        .joinToString(",") { "\"$it\"" }
                                                addStatement(tags)
                                                addStatement("),")
                                            }
                                            addStatement(
                                                "buildNewInstance = { %T() },",
                                                GenerateDynamicFormProcessor.getGeneratedClassNameFor(
                                                    form,
                                                    moduleName
                                                )
                                            )
                                            addStatement(
                                                "mapInstance = { item, registry -> %T(",
                                                GenerateDynamicFormProcessor.getGeneratedClassNameFor(
                                                    form,
                                                    moduleName
                                                )
                                            ).apply {
                                                metadataExtractor.getFieldsMetadataOf(form).forEach { field ->
                                                    addStatement("${field.name} = ")
                                                    add(
                                                        FieldDescriptionFactory.buildObjectToDto(field, "item")
                                                    )
                                                    addStatement(",")
                                                }
                                                addStatement(")")
                                                addStatement("}")
                                            }

                                            addStatement("),")
                                        }
                                    }
                                }
                                .addStatement(")")
                                .addStatement(")")
                                .build()
                        )
                        .build()
                )
                .build()
                .writeTo(codeGenerator, false, allForms.getSources())
        }
    }
}
