package fr.delphes.state

import kotlin.reflect.KClass

class StateManager(vararg states: State) {
    val readOnly = StateProvider(this)

    @PublishedApi
    internal val map = mutableMapOf<KClass<*>, MutableMap<StateIdQualifier, out State>>()

    init {
        states.forEach { put(it) }
    }

    inline fun <reified T : State> getState(id: StateId<T>): T {
        return getStateOrNull(id) ?: throw IllegalStateException("State $id not found")
    }

    inline fun <reified T : State> getStateOrNull(id: StateId<T>): T? {
        return map[T::class]?.get(id.qualifier) as T?
    }

    fun put(state: State): State {
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
        return map[id.clazz]?.get(id.qualifier) as T? ?: return defaultValue().also { put(it) }
    }

    fun <U : State> withState(state: U): StateManager {
        put(state)
        return this
    }
}