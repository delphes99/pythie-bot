package fr.delphes.state

import kotlin.reflect.KClass

class StateManager {
    @PublishedApi
    internal val map = mutableMapOf<KClass<*>, MutableMap<StateId, out State>>()

    inline fun <reified T : State> get(name: StateId): T? {
        return map[T::class]?.get(name) as T?
    }

    inline fun <reified T : State> put(state: T): T {
        if (map[T::class] == null) {
            map[T::class] = mutableMapOf()
        }
        (this.map[T::class] as MutableMap<StateId, State>)[state.id] = state
        return state
    }

    inline fun <reified T : State> getOrPut(name: StateId, defaultValue: () -> T): T {
        val actualValue = map[T::class]?.get(name) ?: return put(defaultValue())
        return actualValue as T
    }

    companion object {
        fun builder() = Builder()

        class Builder {
            @PublishedApi
            internal val instance = StateManager()

            inline fun <reified U : State> addState(state: U): Builder {
                instance.put(state)
                return this
            }

            fun build() = this.instance
        }
    }
}