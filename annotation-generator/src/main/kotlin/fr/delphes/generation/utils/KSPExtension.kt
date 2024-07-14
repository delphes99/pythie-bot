package fr.delphes.generation.utils

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

fun KSTypeReference.hasParent(type: KClass<*>): Boolean {
    val declaration = resolve().declaration

    val hasDirectParent = declaration.qualifiedName?.asString() == type.qualifiedName

    //TODO verify all parents
    return hasDirectParent
}

fun <T : Annotation> KSPropertyDeclaration.getAnnotationValue(
    annotationClass: KClass<T>,
    fieldProperty: KProperty1<T, KClass<*>>,
) = this.annotations
    .firstOrNull { it.isAnnotation(annotationClass) }
    ?.arguments
    ?.firstOrNull { it.name?.getShortName() == fieldProperty.name }

private fun <T : Annotation> KSAnnotation.isAnnotation(annotationClass: KClass<T>): Boolean {
    return shortName.getShortName() == annotationClass.simpleName
            && annotationType.resolve().declaration.qualifiedName?.asString() == annotationClass.qualifiedName
}

fun <T : Annotation> KSClassDeclaration.getAllAnnotations(annotationType: KClass<T>): Sequence<T> {
    return getAnnotationsByType(annotationType).plus(
        superTypes
            .map { superType -> superType.resolve().declaration.closestClassDeclaration() }
            .filterNotNull()
            .flatMap { superType -> superType.getAllAnnotations(annotationType) })
}

fun KSClassDeclaration.toNonAggregatingDependencies() =
    Dependencies(false, containingFile!!)

fun List<KSClassDeclaration>.getSources() = map { it.containingFile as KSFile }