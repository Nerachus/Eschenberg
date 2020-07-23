package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils.clamp
import io.varakh.eb.V_HEIGHT
import io.varakh.eb.V_WIDTH
import io.varakh.eb.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import java.lang.Float.max
import java.lang.Float.min

class MoveSystem : IteratingSystem(
        allOf(TransformComponent::class, MoveComponent::class).exclude(RemovedComponent::class).get()) {

    private var accumulator = 0f

    override fun update(deltaTime: Float) {
        accumulator += deltaTime
        while (accumulator >= UPDATE_RATE) {
            accumulator -= UPDATE_RATE
            super.update(UPDATE_RATE)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity must have a TransformComponent. entity=$entity" }
        val move = entity[MoveComponent.mapper]
        require(move != null) { "Entity must have a MoveComponent. entity=$entity" }

        val player = entity[PlayerComponent.mapper]
        if (player != null) {
            entity[FacingComponent.mapper]?.let { movePlayer(transform, move, it, deltaTime) }
        } else {
            moveEntity(transform, move, deltaTime)
        }
    }

    private fun movePlayer(transform: TransformComponent, move: MoveComponent, facing: FacingComponent,
                           deltaTime: Float) {
        move.speed.x = clampX(when (facing.direction) {
            Direction.LEFT -> min(0f, move.speed.x - H_ACCELERATION * deltaTime)
            Direction.RIGHT -> max(0f, move.speed.x + H_ACCELERATION * deltaTime)
            else -> 0f
        })
        move.speed.y = clampY(when (facing.direction) {
            Direction.UP -> max(0f, move.speed.y + V_ACCELERATION * deltaTime)
            Direction.DOWN -> min(0f, move.speed.y - V_ACCELERATION * deltaTime)
            else -> 0f
        })

        moveEntity(transform, move, deltaTime)
    }

    private fun clampX(move: Float) = clamp(move, -MAX_H_SPEED, MAX_H_SPEED)
    private fun clampY(move: Float) = clamp(move, -MAX_V_SPEED, MAX_V_SPEED)

    private fun moveEntity(transform: TransformComponent, move: MoveComponent, deltaTime: Float) {
        transform.position.x = clamp(transform.position.x + move.speed.x * deltaTime,
                0f, V_WIDTH - transform.size.x)
        transform.position.y = clamp(transform.position.y + move.speed.y * deltaTime,
                0f, V_HEIGHT - transform.size.y)
    }

    companion object {
        const val UPDATE_RATE = 1 / 25f
        const val H_ACCELERATION = 16.5f
        const val V_ACCELERATION = 16.5f
        const val MAX_H_SPEED = 5.5f
        const val MAX_V_SPEED = 5.5f
    }
}