package fr.delphes.generation.dynamicForm

import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.asTypeName
import fr.delphes.annotation.dynamicForm.FieldDescription
import fr.delphes.annotation.dynamicForm.FieldMapper
import fr.delphes.dynamicForm.descriptor.DurationFieldDescriptor
import fr.delphes.dynamicForm.descriptor.MapFieldDescriptor
import fr.delphes.dynamicForm.descriptor.StringFieldDescriptor
import fr.delphes.generation.utils.CompilationCheckException
import fr.delphes.generation.utils.getAnnotationValue
import fr.delphes.utils.serialization.DurationSerializer
import java.time.Duration
import kotlin.reflect.typeOf

fun KSPropertyDeclaration.getFieldMeta(): FieldMetadata {
    val name = simpleName.asString()
    val description = getAnnotationsByType(FieldDescription::class).first().description

    val mapperClass = getAnnotationValue(FieldMapper::class, FieldMapper::mapperClass)?.value as KSType?
    if (mapperClass != null) {
        return FieldWithMapper(name, description, mapperClass)
    }

    return when (this.type.resolve().declaration.qualifiedName?.asString()) {
        "kotlin.String" -> FieldWithType(
            name, description, null, "\"\"", StringFieldDescriptor::class, String::class.asTypeName()
        )

        "java.time.Duration" -> FieldWithType(
            name,
            description,
            DurationSerializer::class,
            "Duration.ZERO",
            DurationFieldDescriptor::class,
            Duration::class.asTypeName()
        )

        "kotlin.collections.Map" -> FieldWithType(
            name,
            description,
            null,
            "emptyMap()",
            MapFieldDescriptor::class,
            typeOf<Map<String, String>>().asTypeName()
        )

        else -> throw CompilationCheckException("Field [${this.simpleName.asString()}] : Unknown type and no mapper")
    }
}