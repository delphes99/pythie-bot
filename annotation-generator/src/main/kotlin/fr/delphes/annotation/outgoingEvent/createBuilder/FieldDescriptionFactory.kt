package fr.delphes.annotation.outgoingEvent.createBuilder

import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import fr.delphes.feature.descriptor.FeatureDescriptor
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
        val fieldInfos = getFieldInfos() ?: error("unable to find fieldInfos for ${simpleName.asString()}")

        return fieldInfos.descriptionClass
    }

    fun buildEncodeValue(builder: FunSpec.Builder, property: KSPropertyDeclaration, stateProviderName: String) {
        val fieldInfos = property.getFieldInfos()
        if (fieldInfos?.mapperClass != null) {
            builder.addCode(
                "%T.map(this.${property.simpleName.asString()}, $stateProviderName)",
                fieldInfos.mapperClass.toClassName()
            )
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
}