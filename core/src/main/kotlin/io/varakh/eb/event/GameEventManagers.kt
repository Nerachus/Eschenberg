package io.varakh.eb.event

import kotlin.reflect.KClass

object GameEventManagers {
    val managers = mutableMapOf<KClass<*>, GameEventManager<*>>()

    @Suppress("UNCHECKED_CAST")
    inline operator fun <reified T : GameEvent> get(type: KClass<T>): GameEventManager<T> {
        val manager = managers[type]
        return if (manager == null) {
            val newManager = GameEventManager(type)
            managers[type] = newManager
            newManager
        } else manager as GameEventManager<T>
    }
}