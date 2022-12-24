package fr.delphes.state

import kotlin.reflect.KClass

class StateManager {
    @PublishedApi
    internal val map = mutableMapOf<KClass<*>, MutableMap<StateId, out State>>()

    inline fun <reified T : State> get(name: StateId): T? {
        return map[T::class]?.get(name) as T?
    }

    inline fun <reified T : State> put(state: T): T {
        return put(T::class, state)
    }

    fun <T: State> put(clazz: KClass<out T>, state: T): T {
        if (map[clazz] == null) {
            map[clazz] = mutableMapOf()
        }
        @Suppress("UNCHECKED_CAST")
        (this.map[clazz] as MutableMap<StateId, State>)[state.id] = state

        return state
    }

    inline fun <reified T : State> getOrPut(name: StateId, defaultValue: () -> T): T {
        val actualValue = map[T::class]?.get(name) ?: return put(defaultValue())
        return actualValue as T
    }


    inline fun <reified U : State> withState(state: U): StateManager {
        put(state)
        return this
    }
}