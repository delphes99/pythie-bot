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
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.OutgoingEventType
import fr.delphes.generation.GenerationUtils.baseGeneratedPackage
import fr.delphes.generation.GenerationUtils.getModuleName
import fr.delphes.generation.GenerationUtils.processEach
import fr.delphes.generation.hasParent
import fr.delphes.generation.toNonAggregatingDependencies
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
                                outgoingEventClass.getAllProperties().forEach { property ->
                                    addParameter(
                                        ParameterSpec.builder(
                                            property.simpleName.asString(),
                                            FieldDescriptionFactory.buildFieldType(property),
                                        )
                                            .defaultValue("\"\"")
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
                        outgoingEventClass.getAllProperties().forEach { property ->
                            addProperty(
                                PropertySpec
                                    .builder(
                                        property.simpleName.asString(),
                                        FieldDescriptionFactory.buildFieldType(property),
                                    )
                                    .initializer(property.simpleName.asString())
                                    .build()
                            )
                        }
                    }
                    .addFunction(
                        FunSpec.builder("description")
                            .addModifiers(KModifier.OVERRIDE)
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
                                outgoingEventClass.getAllProperties().forEach { property ->
                                    FieldDescriptionFactory.buildDescription(this, property)
                                }
                            }
                            .addCode(")")
                            .addCode(")")
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("build")
                            .addModifiers(KModifier.OVERRIDE)
                            .returns(outgoingEventClass.toClassName())
                            .addCode(
                                "return %T(\n",
                                outgoingEventClass.toClassName()
                            )
                            .apply {
                                outgoingEventClass.getAllProperties().forEach { property ->
                                    addCode("${property.simpleName.asString()} = ")
                                    FieldDescriptionFactory.buildEncodeValue(this, property)
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

    private fun checkInheritFromOutgoingEvent(outgoingEventClass: KSClassDeclaration) {
        if (outgoingEventClass.superTypes.none { it.hasParent(OutgoingEvent::class) }) {
            //TODO verify all parents
            logger.info("${outgoingEventClass.qualifiedName?.asString()} must implement OutgoingEvent")
        }
    }


    private fun checkHaveAtLeastOneFieldWithDescription(outgoingEventClass: KSClassDeclaration) {
        if (!outgoingEventClass.getAllProperties().any { it.isAnnotationPresent(FieldDescription::class) }) {
            logger.error("${outgoingEventClass.qualifiedName?.asString()} must have at least one field with description")
        }
    }

    private fun checkAllFieldsHaveDescription(outgoingEventClass: KSClassDeclaration) {
        if (!outgoingEventClass.getAllProperties().all { it.isAnnotationPresent(FieldDescription::class) }) {
            logger.error("${outgoingEventClass.qualifiedName?.asString()} must have all fields with description")
        }
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
