package fr.delphes.annotation.outgoingEvent.createBuilder

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
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEventBuilder
import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.OutgoingEventType
import fr.delphes.generation.CompilationCheckException
import fr.delphes.generation.GenerationUtils.baseGeneratedPackage
import fr.delphes.generation.GenerationUtils.getModuleName
import fr.delphes.generation.GenerationUtils.processEach
import fr.delphes.generation.toNonAggregatingDependencies
import fr.delphes.state.StateProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
        logger.info("Start processing : GenerateOutgoingEventBuilderModuleProcessor")
        return resolver.processEach(logger, RegisterOutgoingEvent::class, ::create)
    }

    private fun create(outgoingEventClass: KSClassDeclaration) {
        checkNoTypeField(outgoingEventClass)
        checkInheritFromOutgoingEvent(outgoingEventClass)
        checkHaveAtLeastOneFieldWithDescription(outgoingEventClass)
        checkAllFieldsHaveDescription(outgoingEventClass)

        val serialName = outgoingEventClass.getAnnotationsByType(RegisterOutgoingEvent::class)
            .first().serializeName
        val builderClass = builderName(outgoingEventClass)
        FileSpec.builder(
            packageName(moduleName),
            builderClass
        )
            .addType(
                TypeSpec.classBuilder(builderClass)
                    .primaryConstructor(
                        FunSpec
                            .constructorBuilder()
                            .apply {
                                outgoingEventClass.getDescriptionFieldsMetadata().forEach { property ->
                                    addParameter(
                                        ParameterSpec.builder(
                                            property.name,
                                            property.fieldType,
                                        )
                                            .defaultValue(property.defaultValue)
                                            .apply {
                                                if (property is FieldWithType && property.serializer != null) {
                                                    addAnnotation(
                                                        AnnotationSpec.builder(Serializable::class)
                                                            .addMember("with = %T::class", property.serializer)
                                                            .build()
                                                    )
                                                }
                                            }
                                            .build(),
                                    )
                                }
                            }
                            .build()
                    )
                    .addAnnotations(
                        listOf(
                            AnnotationSpec.builder(Serializable::class).build(),
                            AnnotationSpec.builder(SerialName::class).addMember("%S", serialName).build(),
                            AnnotationSpec.builder(RegisterOutgoingEventBuilder::class).build(),
                        )
                    )
                    .addSuperinterface(OutgoingEventBuilder::class)
                    .apply {
                        outgoingEventClass.getDescriptionFieldsMetadata().forEach { property ->
                            addProperty(
                                PropertySpec
                                    .builder(
                                        property.name,
                                        property.fieldType,
                                    )
                                    .initializer(property.name)
                                    .build()
                            )
                        }
                    }
                    .addFunction(
                        FunSpec.builder("description")
                            .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
                            .returns(OutgoingEventBuilderDescription::class)
                            .addCode(
                                "return %T(\n",
                                OutgoingEventBuilderDescription::class
                            )
                            .addStatement(
                                "%T(%S),",
                                OutgoingEventType::class,
                                serialName
                            )
                            .addStatement("listOf(")
                            .apply {
                                outgoingEventClass.getDescriptionFieldsMetadata().forEach { property ->
                                    FieldDescriptionFactory.buildDescription(this, property)
                                }
                            }
                            .addCode(")")
                            .addCode(")")
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("build")
                            .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
                            .addParameter(
                                ParameterSpec.builder(
                                    "stateProvider",
                                    StateProvider::class
                                ).build()
                            )
                            .returns(outgoingEventClass.toClassName())
                            .addCode(
                                "return %T(\n",
                                outgoingEventClass.toClassName()
                            )
                            .apply {
                                outgoingEventClass.getDescriptionFieldsMetadata().forEach { property ->
                                    addCode("${property.name} = ")
                                    FieldDescriptionFactory.buildEncodeValue(this, property, "stateProvider")
                                    addCode(",\n")
                                }
                            }
                            .addCode(")")
                            .build()
                    )
                    .build()
            )
            .build()
            .writeTo(codeGenerator, outgoingEventClass.toNonAggregatingDependencies())
    }

    private fun checkNoTypeField(outgoingEventClass: KSClassDeclaration) {
        if (!outgoingEventClass.getDescriptionFields().none { it.simpleName.asString() == "type" }) {
            throw CompilationCheckException("${outgoingEventClass.qualifiedName?.asString()} must no have a field named 'type'")
        }
    }

    private fun checkInheritFromOutgoingEvent(outgoingEventClass: KSClassDeclaration) {
        //TODO verify all parents
        //if (outgoingEventClass.superTypes.none { it.hasParent(OutgoingEvent::class) }) {
        //    throw CompilationCheckException("${outgoingEventClass.qualifiedName?.asString()} must implement OutgoingEvent")
        //}
    }


    private fun checkHaveAtLeastOneFieldWithDescription(outgoingEventClass: KSClassDeclaration) {
        if (!outgoingEventClass.getDescriptionFields().any { it.isAnnotationPresent(FieldDescription::class) }) {
            throw CompilationCheckException("${outgoingEventClass.qualifiedName?.asString()} must have at least one field with description")
        }
    }

    private fun checkAllFieldsHaveDescription(outgoingEventClass: KSClassDeclaration) {
        if (!outgoingEventClass.getDescriptionFields().all { it.isAnnotationPresent(FieldDescription::class) }) {
            throw CompilationCheckException("${outgoingEventClass.qualifiedName?.asString()} must have all fields with description")
        }
    }

    private fun KSClassDeclaration.getDescriptionFieldsMetadata(): Sequence<FieldMetadata> {
        return getDescriptionFields().map(KSPropertyDeclaration::getFieldMeta)
    }

    private fun KSClassDeclaration.getDescriptionFields(): Sequence<KSPropertyDeclaration> {
        val constructorParameterNames = primaryConstructor
            ?.parameters
            ?.mapNotNull { it.name }
            ?: emptyList()

        return getAllProperties().filter { it.simpleName in constructorParameterNames }
    }

    companion object {
        fun packageName(moduleName: String) = "${baseGeneratedPackage(moduleName)}.outgoingEvent"

        fun builderName(eventClass: KSClassDeclaration) = "${eventClass.simpleName.asString()}Builder"

        fun builderClassName(moduleName: String, eventClass: KSClassDeclaration) = ClassName(
            packageName(moduleName),
            builderName(eventClass),
        )
    }
}
