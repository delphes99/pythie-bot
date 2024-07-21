package fr.delphes.generation.dynamicForm.metada.type.string

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.asTypeName
import fr.delphes.dynamicForm.descriptor.StringFieldDescriptor
import fr.delphes.generation.dynamicForm.metada.FieldMetadata
import fr.delphes.generation.dynamicForm.metada.FieldWithType
import fr.delphes.generation.dynamicForm.metada.TypeMetadataExtractor
import fr.delphes.generation.dynamicForm.metada.getDescription
import fr.delphes.generation.dynamicForm.metada.getSimpleName

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