package fr.delphes.generation

import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
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

fun KSClassDeclaration.getSerialName() =
    getAnnotationsByType(RegisterOutgoingEvent::class)
        .first().serializeName

fun KSClassDeclaration.toNonAggregatingDependencies() =
    Dependencies(false, containingFile!!)