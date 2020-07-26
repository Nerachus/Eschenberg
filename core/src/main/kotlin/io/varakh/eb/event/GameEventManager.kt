package io.varakh.eb.event

import ktx.collections.GdxSet
import ktx.log.debug
import ktx.log.logger
import kotlin.reflect.KClass

private val log = logger<GameEventManager<*>>()

class GameEventManager<T : GameEvent>() {
    @Suppress("UNUSED_PARAMETER")
    constructor(type: KClass<T>) : this()

    private val listeners = GdxSet<GameEventListener<T>>()

    fun addEventListener(listener: GameEventListener<T>) {
        listeners.add(listener)
    }

    fun removeEventListener(listener: GameEventListener<T>) {
        listeners.remove(listener)
    }

    fun dispatchEvent(event: T) {
        log.debug { "Dispatching event $event" }
        listeners.forEach { it.onEvent(event) }
    }
}