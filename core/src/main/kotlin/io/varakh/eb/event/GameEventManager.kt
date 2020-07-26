package io.varakh.eb.event

import ktx.collections.GdxSet
import kotlin.reflect.KClass

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
        listeners.forEach { it.onEvent(event) }
    }
}