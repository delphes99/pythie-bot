package fr.delphes.annotation.outgoingEvent.createBuilder

import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.descriptor.DurationFeatureDescriptor
import fr.delphes.feature.descriptor.FeatureDescriptor
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import fr.delphes.generation.getAnnotationValue
import fr.delphes.utils.serialization.DurationSerializer
import kotlin.reflect.KClass

object FieldDescriptionFactory {
    fun buildFieldType(property: KSPropertyDeclaration): TypeName {
        val fieldInfos = property.getFieldInfos()
        return fieldInfos?.mapperClass?.let {
            return ClassName("kotlin", "String")
        } ?: property.type.toTypeName()
    }

    fun buildDescription(builder: FunSpec.Builder, property: KSPropertyDeclaration) {
        with(builder) {
            addCode("%T(", property.toDescriptorClass())
            addStatement("fieldName=\"${property.simpleName.asString()}\",")
            property.getAnnotationsByType(FieldDescription::class).first().let {
                addStatement("description=\"${it.description}\",")
            }
            addStatement("value=${property.simpleName.asString()},")
            addCode("),\n")
        }
    }

    private fun KSPropertyDeclaration.toDescriptorClass(): KClass<out FeatureDescriptor> {
        return getFieldInfos()
            ?.descriptionClass
            ?: error("${this.simpleName.asString()} must have a mapper")
    }

    private fun KSPropertyDeclaration.getFieldInfos(): FieldInfos? {
        val className = type.resolve().declaration.qualifiedName?.asString()

        return this.getFieldDescriptionMapper()
            ?.let(FieldInfos.Companion::of)
            ?: typeToDescriptor[className]
    }

    private fun KSPropertyDeclaration.getFieldDescriptionMapper(): KSType? {
        return getAnnotationValue(FieldMapper::class, FieldMapper::mapperClass)?.value as KSType?
    }

    fun buildEncodeValue(builder: FunSpec.Builder, property: KSPropertyDeclaration) {
        val fieldInfos = property.getFieldInfos()
        if (fieldInfos?.mapperClass != null) {
            builder.addCode("%T.map(this.${property.simpleName.asString()})", fieldInfos.mapperClass.toClassName())
        } else {
            builder.addCode("this.${property.simpleName.asString()}")
        }
    }

    fun buildFieldDefaultValue(property: KSPropertyDeclaration): String {
        val fieldInfos =
            property.getFieldInfos() ?: error("unable to find fieldInfos for ${property.simpleName.asString()}")
        return fieldInfos.defaultValue
    }

    fun buildFieldSerializer(property: KSPropertyDeclaration): ClassName? {
        val fieldInfos =
            property.getFieldInfos() ?: error("unable to find fieldInfos for ${property.simpleName.asString()}")
        return fieldInfos.fieldSerializer
    }

    private val typeToDescriptor = mapOf(
        "kotlin.String" to FieldInfos(StringFeatureDescriptor::class),
        "java.time.Duration" to FieldInfos(DurationFeatureDescriptor::class),
    )
}

class FieldInfos(
    val descriptionClass: KClass<out FeatureDescriptor>,
    val mapperClass: KSType? = null,
) {
    val defaultValue: String
        get() = when (descriptionClass) {
            StringFeatureDescriptor::class -> "\"\""
            DurationFeatureDescriptor::class -> "Duration.ZERO"
            OutgoingEventBuilderDescription::class -> "listOf()"
            else -> error("Unknown default value for $descriptionClass")
        }

    val fieldSerializer: ClassName?
        get() = when (descriptionClass) {
            DurationFeatureDescriptor::class -> DurationSerializer::class.asTypeName()
            StringFeatureDescriptor::class,
            OutgoingEventBuilderDescription::class,
            -> null

            else -> error("Unknown serializer for $descriptionClass")
        }

    companion object {
        fun of(fieldMapper: KSType): FieldInfos {
            return FieldInfos(
                StringFeatureDescriptor::class,
                fieldMapper
            )
        }
    }
}