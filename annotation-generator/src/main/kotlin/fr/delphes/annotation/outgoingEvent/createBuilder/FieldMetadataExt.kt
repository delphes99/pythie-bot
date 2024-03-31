package fr.delphes.annotation.outgoingEvent.createBuilder

import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.asTypeName
import fr.delphes.feature.descriptor.DurationFeatureDescriptor
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import fr.delphes.generation.CompilationCheckException
import fr.delphes.generation.getAnnotationValue
import fr.delphes.utils.serialization.DurationSerializer
import java.time.Duration

fun KSPropertyDeclaration.getFieldMeta(): FieldMetadata {
    val name = simpleName.asString()
    val description = getAnnotationsByType(FieldDescription::class).first().description

    val mapperClass = getAnnotationValue(FieldMapper::class, FieldMapper::mapperClass)?.value as KSType?
    if (mapperClass != null) {
        return FieldWithMapper(name, description, mapperClass)
    }

    return when (this.type.resolve().declaration.qualifiedName?.asString()) {
        "kotlin.String" -> FieldWithType(
            name, description, null, "\"\"", StringFeatureDescriptor::class, String::class.asTypeName()
        )

        "java.time.Duration" -> FieldWithType(
            name,
            description,
            DurationSerializer::class,
            "Duration.ZERO",
            DurationFeatureDescriptor::class,
            Duration::class.asTypeName()
        )

        else -> throw CompilationCheckException("Field [${this.simpleName.asString()}] : Unknown type and no mapper")
    }
}