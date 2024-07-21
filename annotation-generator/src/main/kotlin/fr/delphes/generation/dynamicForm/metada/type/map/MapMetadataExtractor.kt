package fr.delphes.generation.dynamicForm.metada.type.map

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import fr.delphes.dynamicForm.descriptor.MapFieldDescriptor
import fr.delphes.generation.dynamicForm.metada.FieldMetadata
import fr.delphes.generation.dynamicForm.metada.FieldWithType
import fr.delphes.generation.dynamicForm.metada.MetadataExtractor
import fr.delphes.generation.dynamicForm.metada.getDescription
import fr.delphes.generation.dynamicForm.metada.getSimpleName

object MapMetadataExtractor : MetadataExtractor {
    override val typeName = "kotlin.collections.Map"

    override fun extract(declaration: KSPropertyDeclaration): FieldMetadata {
        return FieldWithType(
            declaration.getSimpleName(),
            declaration.getDescription(),
            null,
            "emptyMap()",
            MapFieldDescriptor::class,
            Map::class.asClassName().parameterizedBy(String::class.asTypeName(), String::class.asTypeName())
        )
    }
}