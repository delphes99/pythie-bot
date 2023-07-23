package fr.delphes.annotation.outgoingEvent

import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.FunSpec
import fr.delphes.feature.descriptor.DurationFeatureDescriptor
import fr.delphes.feature.descriptor.FeatureDescriptor
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import kotlin.reflect.KClass

object FieldDescriptionFactory {
    fun build(builder: FunSpec.Builder, property: KSPropertyDeclaration) {
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
        val className = type.resolve().declaration.qualifiedName?.asString()
        return typeToDescriptor[className] ?: error("Unknown type $className")
    }

    private val typeToDescriptor = mapOf(
        "kotlin.String" to StringFeatureDescriptor::class,
        "java.time.Duration" to DurationFeatureDescriptor::class,
    )
}