package fr.delphes.generation

import com.google.devtools.ksp.symbol.KSTypeReference
import kotlin.reflect.KClass

fun KSTypeReference.hasParent(type: KClass<*>): Boolean {
    val declaration = resolve().declaration

    val hasDirectParent = declaration.qualifiedName?.asString() == type.qualifiedName

    //TODO verify all parents
    return hasDirectParent
}