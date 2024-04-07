package fr.delphes.generation

import java.net.URLClassLoader

fun Any.getFieldValue(propertyName: String): Any? {
    val getterName = "get" + propertyName.capitalize()
    return try {
        javaClass.getMethod(getterName).invoke(this)
    } catch (e: NoSuchMethodException) {
        null
    }
}

fun URLClassLoader.loadGlobalVariable(packageName: String, variableName: String): Any {
    val fileClass =
        loadClass("$packageName.${variableName.replaceFirstChar { it.uppercase() }}Kt")
    return fileClass
        .getDeclaredField(variableName)
        .apply { isAccessible = true }
        .get(null)
}