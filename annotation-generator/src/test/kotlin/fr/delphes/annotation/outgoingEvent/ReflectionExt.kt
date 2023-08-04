package fr.delphes.annotation.outgoingEvent

fun Any.getFieldValue(propertyName: String): Any? {
    val getterName = "get" + propertyName.capitalize()
    return try {
        javaClass.getMethod(getterName).invoke(this)
    } catch (e: NoSuchMethodException) {
        null
    }
}