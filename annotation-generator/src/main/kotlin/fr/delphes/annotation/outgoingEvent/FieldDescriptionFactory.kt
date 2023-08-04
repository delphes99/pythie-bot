package fr.delphes.annotation.outgoingEvent

import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toTypeName
import fr.delphes.feature.descriptor.DurationFeatureDescriptor
import fr.delphes.feature.descriptor.FeatureDescriptor
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import fr.delphes.generation.getAnnotationValue
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

    private val typeToDescriptor = mapOf(
        "kotlin.String" to FieldInfos(StringFeatureDescriptor::class),
        "java.time.Duration" to FieldInfos(DurationFeatureDescriptor::class),
    )
}

class FieldInfos(
    val descriptionClass: KClass<out FeatureDescriptor>,
    val mapperClass: KSType? = null,
) {
    companion object {
        fun of(fieldMapper: KSType): FieldInfos {
            return FieldInfos(
                StringFeatureDescriptor::class,
                fieldMapper
            )
        }
    }
}