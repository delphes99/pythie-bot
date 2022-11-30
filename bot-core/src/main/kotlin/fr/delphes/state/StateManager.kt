package fr.delphes.state

import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import kotlin.reflect.KClass

class StateManager(
    val clock: Clock = SystemClock
) {
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


    inline fun <reified U : State> withState(state: U): StateManager {
        put(state)
        return this
    }
}