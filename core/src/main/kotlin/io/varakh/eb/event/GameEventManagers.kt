package io.varakh.eb.event

import kotlin.reflect.KClass

object GameEventManagers {
    val managers = mutableMapOf<KClass<*>, GameEventManager<*>>()

    @Suppress("UNCHECKED_CAST")
    inline operator fun <reified T : GameEvent> get(type: KClass<T>): GameEventManager<T> =
            managers[type] as GameEventManager<T>? ?: GameEventManager(type).also {
                managers[type] = it
            }
}