package fr.delphes.generation.dynamicForm.metadata.type.string

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.asTypeName
import fr.delphes.dynamicForm.descriptor.StringFieldDescriptor
import fr.delphes.generation.dynamicForm.metadata.FieldMetadata
import fr.delphes.generation.dynamicForm.metadata.FieldWithType
import fr.delphes.generation.dynamicForm.metadata.TypeMetadataExtractor
import fr.delphes.generation.dynamicForm.metadata.getDescription
import fr.delphes.generation.dynamicForm.metadata.getSimpleName

object StringMetadaExtractor : TypeMetadataExtractor {
    override val typeName = "kotlin.String"

    override fun extract(declaration: KSPropertyDeclaration): FieldMetadata {
        return FieldWithType(
            declaration.getSimpleName(),
            declaration.getDescription(),
            null,
            "\"\"",
            StringFieldDescriptor::class,
            String::class.asTypeName()
        )
    }
}