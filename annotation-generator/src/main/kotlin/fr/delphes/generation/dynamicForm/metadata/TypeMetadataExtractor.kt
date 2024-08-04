package fr.delphes.generation.dynamicForm.metadata

import com.google.devtools.ksp.symbol.KSPropertyDeclaration

interface TypeMetadataExtractor {
    val typeName: String
    fun extract(declaration: KSPropertyDeclaration): FieldMetadata
}