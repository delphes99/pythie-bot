package fr.delphes.generation.dynamicForm.metadata

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import fr.delphes.annotation.dynamicForm.FieldMapper
import fr.delphes.generation.dynamicForm.metadata.type.duration.DurationMetadaExtractor
import fr.delphes.generation.dynamicForm.metadata.type.list.ListMetadataExtractor
import fr.delphes.generation.dynamicForm.metadata.type.string.StringMetadaExtractor
import fr.delphes.generation.utils.CompilationCheckException
import fr.delphes.generation.utils.getAnnotationValue

class MetadataExtractor {
    private val metadataExtractors = listOf(
        StringMetadaExtractor,
        DurationMetadaExtractor,
        ListMetadataExtractor
    )

    fun getFieldMetaOf(declaration: KSPropertyDeclaration): FieldMetadata {
        val mapperClass = declaration.getAnnotationValue(FieldMapper::class, FieldMapper::mapperClass)?.value as KSType?
        if (mapperClass != null) {
            return FieldWithMapper(declaration.getSimpleName(), declaration.getDescription(), mapperClass)
        }

        val typeName = declaration.type.resolve().declaration.qualifiedName?.asString()

        return metadataExtractors
            .firstOrNull { it.typeName == typeName }
            ?.extract(declaration)
            ?: throw CompilationCheckException("Field [${declaration.getSimpleName()}] : Unknown type and no mapper")
    }

    fun getFieldsMetadataOf(declaration: KSClassDeclaration): Sequence<FieldMetadata> {
        return declaration.getDescriptionFields().map(this::getFieldMetaOf)
    }
}