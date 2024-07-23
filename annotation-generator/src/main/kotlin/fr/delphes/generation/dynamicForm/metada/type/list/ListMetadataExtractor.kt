package fr.delphes.generation.dynamicForm.metada.type.list

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import fr.delphes.annotation.dynamicForm.DynamicFormParent
import fr.delphes.generation.dynamicForm.metada.FieldMetadata
import fr.delphes.generation.dynamicForm.metada.FieldWithFormList
import fr.delphes.generation.dynamicForm.metada.TypeMetadataExtractor
import fr.delphes.generation.dynamicForm.metada.getDescription
import fr.delphes.generation.dynamicForm.metada.getSimpleName
import fr.delphes.generation.utils.CompilationCheckException
import fr.delphes.generation.utils.getAllAnnotations

object ListMetadataExtractor : TypeMetadataExtractor {
    override val typeName = "kotlin.collections.List"

    override fun extract(declaration: KSPropertyDeclaration): FieldMetadata {
        val parentAnnotation = declaration
            .type
            .resolve()
            .arguments
            .first()
            .type
            ?.resolve()
            ?.declaration
            ?.closestClassDeclaration()
            ?.getAllAnnotations(DynamicFormParent::class)
            ?.firstOrNull()

        if (parentAnnotation == null) {
            throw CompilationCheckException("Field [${declaration.getSimpleName()}] : Only list of dynamic form with parent is supported")
        }

        return FieldWithFormList(
            declaration.getSimpleName(),
            declaration.getDescription(),
            parentAnnotation.family,
            declaration.type.resolve().arguments.first().type!!.resolve()
        )
    }
}