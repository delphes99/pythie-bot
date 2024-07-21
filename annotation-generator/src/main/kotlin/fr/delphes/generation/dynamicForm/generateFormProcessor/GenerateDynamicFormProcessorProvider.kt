package fr.delphes.generation.dynamicForm.generateFormProcessor

import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.FieldDescription
import fr.delphes.annotation.dynamicForm.RegisterDynamicFormDto
import fr.delphes.dynamicForm.DynamicFormDTO
import fr.delphes.dynamicForm.DynamicFormDescription
import fr.delphes.dynamicForm.DynamicFormType
import fr.delphes.generation.dynamicForm.metada.FieldWithType
import fr.delphes.generation.dynamicForm.metada.MetadataExtractor
import fr.delphes.generation.dynamicForm.metada.getDescriptionFields
import fr.delphes.generation.outgoingEvent.generateBuilderProcessor.FieldDescriptionFactory
import fr.delphes.generation.utils.CompilationCheckException
import fr.delphes.generation.utils.GenerationUtils.baseGeneratedPackage
import fr.delphes.generation.utils.GenerationUtils.getModuleName
import fr.delphes.generation.utils.GenerationUtils.processEach
import fr.delphes.generation.utils.toNonAggregatingDependencies
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class GenerateDynamicFormProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return GenerateDynamicFormProcessor(
            environment.codeGenerator,
            environment.logger,
            getModuleName(environment),
        )
    }
}

class GenerateDynamicFormProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    moduleName: String,
) : SymbolProcessor {
    private val formAnnotationClass = DynamicForm::class
    private val packageName = packageNameOf(moduleName)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("Start processing : Generate Dynamic Form")
        return resolver.processEach(logger, formAnnotationClass, ::create)
    }

    private fun create(baseFormClass: KSClassDeclaration) {
        val metadataExtractor = MetadataExtractor()

        checkFieldsHaveDescription(baseFormClass)

        val serialName = baseFormClass.getAnnotationsByType(formAnnotationClass).first().name
        val generatedClassName = generatedClassNameOf(baseFormClass)
        FileSpec.builder(packageName, generatedClassName)
            .addType(
                TypeSpec.classBuilder(generatedClassName).primaryConstructor(FunSpec.constructorBuilder().apply {
                    metadataExtractor.getFieldsMetadataOf(baseFormClass).forEach { property ->
                        addParameter(
                            ParameterSpec.builder(
                                property.name,
                                property.fieldType
                            ).defaultValue(property.defaultValue).apply {
                                if (property is FieldWithType && property.serializer != null) {
                                    addAnnotation(
                                        AnnotationSpec.builder(Serializable::class)
                                            .addMember("with = %T::class", property.serializer).build()
                                    )
                                }
                            }.build(),
                        )
                    }
                }.build()).addAnnotations(
                    listOf(
                        AnnotationSpec.builder(Serializable::class).build(),
                        AnnotationSpec.builder(SerialName::class).addMember("%S", serialName).build(),
                        AnnotationSpec.builder(RegisterDynamicFormDto::class).build(),
                    )
                ).addSuperinterface(
                    DynamicFormDTO::class.asClassName().plusParameter(baseFormClass.toClassName())
                ).apply {
                    metadataExtractor.getFieldsMetadataOf(baseFormClass).forEach { property ->
                        addProperty(
                            PropertySpec.builder(
                                property.name,
                                property.fieldType,
                            ).initializer(property.name).build()
                        )
                    }
                }.addProperty(
                    PropertySpec.builder("formType", DynamicFormType::class)
                        .initializer("DynamicFormType(%S)", serialName)
                        .build()
                ).addFunction(
                    FunSpec.builder("description")
                        .addModifiers(KModifier.OVERRIDE)
                        .returns(DynamicFormDescription::class)
                        .addCode(
                            "return %T(\n",
                            DynamicFormDescription::class
                        )
                        .addStatement("formType,")
                        .addStatement("listOf(")
                        .apply {
                            metadataExtractor.getFieldsMetadataOf(baseFormClass).forEach { property ->
                                FieldDescriptionFactory.buildDescription(this, property)
                            }
                        }
                        .addCode(")")
                        .addCode(")")
                        .build()
                ).addFunction(
                    FunSpec.builder("build")
                        .addModifiers(KModifier.OVERRIDE)
                        .returns(baseFormClass.toClassName())
                        .addCode("return %T(\n", baseFormClass.toClassName())
                        .apply {
                            metadataExtractor.getFieldsMetadataOf(baseFormClass).forEach { property ->
                                addCode("${property.name} = ")
                                addCode(FieldDescriptionFactory.buildDtoToObject(property))
                                addCode(",\n")
                            }
                        }
                        .addCode(")")
                        .build()
                ).build()
            )
            .build().writeTo(codeGenerator, baseFormClass.toNonAggregatingDependencies())
    }

    private fun checkFieldsHaveDescription(formClass: KSClassDeclaration) {
        val (fieldWithDescription, fieldWithMissingDescription) = formClass.getDescriptionFields()
            .partition { it.isAnnotationPresent(FieldDescription::class) }
        if (fieldWithDescription.isEmpty()) {
            throw CompilationCheckException("${formClass.qualifiedName?.asString()} must have at least one field with description")
        }
        if (fieldWithMissingDescription.isNotEmpty()) {
            val fieldsInError = fieldWithMissingDescription.joinToString(", ") { "\"${it.simpleName.asString()}\"" }
            throw CompilationCheckException("""${formClass.qualifiedName?.asString()} : missing description on fields : [$fieldsInError]""")
        }
    }

    companion object {
        private fun packageNameOf(moduleName: String) = "${baseGeneratedPackage(moduleName)}.dynamicForm"

        private fun generatedClassNameOf(baseFormClass: KSClassDeclaration) =
            "${baseFormClass.simpleName.asString()}DynamicForm"

        fun getGeneratedClassNameFor(baseFormClass: KSClassDeclaration, moduleName: String) =
            ClassName(packageNameOf(moduleName), generatedClassNameOf(baseFormClass))
    }
}