package io.varakh.eb.event

import kotlin.reflect.KClass

object GameEventManagers {
    private val managers = mutableMapOf<KClass<*>, GameEventManager<*>>()

    @Suppress("UNCHECKED_CAST")
    operator fun <T : GameEvent> get(type: KClass<T>) =
            managers[type] as GameEventManager<T>?
                    ?: GameEventManager(type).also { managers[type] = it }
}