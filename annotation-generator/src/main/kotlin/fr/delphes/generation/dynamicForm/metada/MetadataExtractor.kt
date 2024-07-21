package fr.delphes.generation.dynamicForm.metada

import com.google.devtools.ksp.symbol.KSPropertyDeclaration

interface MetadataExtractor {
    val typeName: String
    fun extract(declaration: KSPropertyDeclaration): FieldMetadata
}