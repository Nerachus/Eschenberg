package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import io.varakh.eb.ecs.component.PlayerComponent
import io.varakh.eb.ecs.component.RemovedComponent
import io.varakh.eb.ecs.component.TransformComponent
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.log.logger
import java.lang.Float.max

private val log = logger<DamageSystem>()

class DamageSystem : IteratingSystem(
        allOf(PlayerComponent::class, TransformComponent::class).exclude(RemovedComponent::class).get()) {

    private var accumulator = 0f

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val player = entity[PlayerComponent.mapper]
        require(player != null) { "Entity must have a PlayerComponent. entity=$entity" }
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity must have a TransformComponent. entity=$entity" }

        if (transform.position.y > DAMAGE_AREA_HEIGHT) return

        var damage = DAMAGE_PER_SECOND * deltaTime
        if (player.shield > 0f) {
            val blockAmount = player.shield
            player.shield = max(0f, player.shield - damage)
            damage -= blockAmount
            if (damage <= 0) return
        }
        player.health = max(0f, player.health - damage)
        if (player.health <= 0f) entity.addComponent<RemovedComponent>(engine) {
            delay = DEATH_EXPLOSION_DURATION
        }
    }

    companion object {
        private const val DAMAGE_AREA_HEIGHT = 2f
        private const val DAMAGE_PER_SECOND = 25f
        private const val DEATH_EXPLOSION_DURATION = 0.9f
    }
}