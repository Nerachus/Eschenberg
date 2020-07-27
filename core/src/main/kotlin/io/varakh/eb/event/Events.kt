package io.varakh.eb.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import io.varakh.eb.ecs.component.PowerUpType

sealed class GameEvent : Pool.Poolable

object NonEntity : Entity()

interface GameEventListener<T : GameEvent> {
    fun onEvent(event: T)
}

data class PlayerDeathEvent(var points: Float = 0f) : GameEvent() {
    override fun reset() {
        points = 0f
    }

    override fun toString() = "GameEventPlayerDeath(distance=$points)"
}

data class CollectPowerUpEvent(var player: Entity = NonEntity,
                               var type: PowerUpType = PowerUpType.NONE) : GameEvent() {
    override fun reset() {
        player = NonEntity
        type = PowerUpType.NONE
    }

    override fun toString() = "GameEventCollectPowerUp(player=$player, type=$type)"
}

data class PlayerDamageEvent(var player: Entity = NonEntity,
                             var health: Float = 0f) : GameEvent() {
    override fun reset() {
        player = NonEntity
        health = 0f
    }

    override fun toString() = "PlayerDamageEvent(player=$player, health=$health)"
}