package io.varakh.eb.event


import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools
import io.varakh.eb.util.pooled
import ktx.collections.GdxSet
import ktx.log.debug
import ktx.log.logger
import kotlin.reflect.KClass

val log = logger<GameEventManager<*>>()

class GameEventManager<T : GameEvent>(type: KClass<T>) {

    val listeners = GdxSet<GameEventListener<T>>()
    val eventPool: Pool<T> = Pools.get(type.java)

    fun addEventListener(listener: GameEventListener<T>) {
        listeners.add(listener)
    }

    fun removeEventListener(listener: GameEventListener<T>) {
        listeners.remove(listener)
    }

    inline fun dispatchEvent(block: T.() -> Unit) {
        eventPool.pooled { event ->
            event.block()
            log.debug { "Dispatching event $event" }
            listeners.forEach { it.onEvent(event) }
        }
    }
}
