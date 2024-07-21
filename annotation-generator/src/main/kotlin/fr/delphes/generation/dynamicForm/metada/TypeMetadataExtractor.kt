package fr.delphes.generation.dynamicForm.metada

import com.google.devtools.ksp.symbol.KSPropertyDeclaration

interface TypeMetadataExtractor {
    val typeName: String
    fun extract(declaration: KSPropertyDeclaration): FieldMetadata
}