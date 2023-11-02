package fr.delphes.annotation.outgoingEvent.createBuilder

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import fr.delphes.feature.descriptor.DurationFeatureDescriptor
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import fr.delphes.generation.getAnnotationValue

fun KSPropertyDeclaration.getFieldInfos(): FieldInfos? {
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