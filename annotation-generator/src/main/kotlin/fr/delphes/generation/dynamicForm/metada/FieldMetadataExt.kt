package fr.delphes.generation.dynamicForm.metada

import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import fr.delphes.annotation.dynamicForm.FieldDescription

fun KSPropertyDeclaration.getSimpleName() = simpleName.asString()

fun KSPropertyDeclaration.getDescription() = getAnnotationsByType(FieldDescription::class).first().description

fun KSClassDeclaration.getDescriptionFields(): Sequence<KSPropertyDeclaration> {
    val constructorParameterNames = primaryConstructor
        ?.parameters
        ?.mapNotNull { it.name }
        ?: emptyList()

    return getAllProperties().filter { it.simpleName in constructorParameterNames }
}