package io.varakh.eb.event

import com.badlogic.ashley.core.Entity
import io.varakh.eb.ecs.component.PowerUpType

sealed class GameEvent

interface GameEventListener<T : GameEvent> {
    fun onEvent(event: T)
}

data class PlayerDeathEvent(val distance: Float = 0f) : GameEvent() {
    override fun toString() = "GameEventPlayerDeath(distance=$distance"
}

data class CollectPowerUpEvent(val player: Entity,
                               val type: PowerUpType) : GameEvent() {
    override fun toString() = "GameEventCollectPowerUp(player=$player, type=$type"
}