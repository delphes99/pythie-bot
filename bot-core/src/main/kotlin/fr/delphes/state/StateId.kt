package fr.delphes.state

import kotlin.reflect.KClass

data class StateId<T : State>(
    val clazz: KClass<T>,
    val qualifier: StateIdQualifier
) {
    companion object {
        inline fun <reified T : State> from(id: StateIdQualifier) = StateId(T::class, id)

        inline fun <reified T : State> from(id: String) = from<T>(StateIdQualifier(id))
    }
}