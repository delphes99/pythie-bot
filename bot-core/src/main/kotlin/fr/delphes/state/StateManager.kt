package fr.delphes.state

import kotlin.reflect.KClass

class StateManager {
    @PublishedApi
    internal val map = mutableMapOf<KClass<*>, MutableMap<StateIdQualifier, out State>>()

    inline fun <reified T : State> getState(id: StateId<T>): T? {
        return map[T::class]?.get(id.qualifier) as T?
    }

    fun <T: State> put(state: T): T {
        val (clazz, qualifier) = state.id
        if (map[clazz] == null) {
            map[clazz] = mutableMapOf()
        }
        @Suppress("UNCHECKED_CAST")
        (this.map[clazz] as MutableMap<StateIdQualifier, State>)[qualifier] = state

        return state
    }

    fun <T : State> getOrPut(id: StateId<T>, defaultValue: () -> T): T {
        @Suppress("UNCHECKED_CAST")
        return map[id.clazz]?.get(id.qualifier) as T? ?: return put(defaultValue())
    }

    fun <U : State> withState(state: U): StateManager {
        put(state)
        return this
    }
}