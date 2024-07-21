package fr.delphes.generation.dynamicForm.metada

import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import fr.delphes.annotation.dynamicForm.FieldDescription
import fr.delphes.annotation.dynamicForm.FieldMapper
import fr.delphes.generation.dynamicForm.metada.type.duration.DurationMetadaExtractor
import fr.delphes.generation.dynamicForm.metada.type.list.ListMetadataExtractor
import fr.delphes.generation.dynamicForm.metada.type.map.MapMetadataExtractor
import fr.delphes.generation.dynamicForm.metada.type.string.StringMetadaExtractor
import fr.delphes.generation.utils.CompilationCheckException
import fr.delphes.generation.utils.getAnnotationValue

private val metadataExtractors = listOf(
    StringMetadaExtractor,
    DurationMetadaExtractor,
    MapMetadataExtractor,
    ListMetadataExtractor
)

fun KSPropertyDeclaration.getFieldMeta(): FieldMetadata {
    val mapperClass = getAnnotationValue(FieldMapper::class, FieldMapper::mapperClass)?.value as KSType?
    if (mapperClass != null) {
        return FieldWithMapper(getSimpleName(), getDescription(), mapperClass)
    }

    val typeName = this.type.resolve().declaration.qualifiedName?.asString()

    return metadataExtractors
        .firstOrNull { it.typeName == typeName }
        ?.extract(this)
        ?: throw CompilationCheckException("Field [${getSimpleName()}] : Unknown type and no mapper")
}

fun KSPropertyDeclaration.getSimpleName() = simpleName.asString()

fun KSPropertyDeclaration.getDescription() = getAnnotationsByType(FieldDescription::class).first().description

fun KSClassDeclaration.getDescriptionFieldsMetadata(): Sequence<FieldMetadata> {
    return getDescriptionFields().map(KSPropertyDeclaration::getFieldMeta)
}

fun KSClassDeclaration.getDescriptionFields(): Sequence<KSPropertyDeclaration> {
    val constructorParameterNames = primaryConstructor
        ?.parameters
        ?.mapNotNull { it.name }
        ?: emptyList()

    return getAllProperties().filter { it.simpleName in constructorParameterNames }
}