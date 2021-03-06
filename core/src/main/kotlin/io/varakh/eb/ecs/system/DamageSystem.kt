package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import io.varakh.eb.ecs.component.PlayerComponent
import io.varakh.eb.ecs.component.RemoveComponent
import io.varakh.eb.ecs.component.TransformComponent
import io.varakh.eb.event.GameEventManagers
import io.varakh.eb.event.PlayerDamageEvent
import io.varakh.eb.event.PlayerDeathEvent
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import java.lang.Float.max

class DamageSystem : IteratingSystem(
        allOf(PlayerComponent::class, TransformComponent::class).exclude(RemoveComponent::class).get()) {

    private val deathEventManager = GameEventManagers[PlayerDeathEvent::class]
    private val playerDamageManager = GameEventManagers[PlayerDamageEvent::class]

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val playerComp = entity[PlayerComponent.mapper]!!
        val transform = entity[TransformComponent.mapper]!!

        if (transform.position.y > DAMAGE_AREA_HEIGHT) return

        var damage = DAMAGE_PER_SECOND * deltaTime
        if (playerComp.shield > 0f) {
            val blockAmount = playerComp.shield
            playerComp.shield = max(0f, playerComp.shield - damage)
            damage -= blockAmount
            if (damage <= 0) return
        }
        playerComp.health = max(0f, playerComp.health - damage)
        playerDamageManager.dispatchEvent {
            player = entity
            health = playerComp.health
        }

        if (playerComp.health <= 0f) {
            deathEventManager.dispatchEvent { points = playerComp.points }
            entity.addComponent<RemoveComponent>(engine) {
                delay = DEATH_EXPLOSION_DURATION
            }
        }
    }

    companion object {
        const val DAMAGE_AREA_HEIGHT = 1f
        private const val DAMAGE_PER_SECOND = 25f
        private const val DEATH_EXPLOSION_DURATION = 0.5f
    }
}