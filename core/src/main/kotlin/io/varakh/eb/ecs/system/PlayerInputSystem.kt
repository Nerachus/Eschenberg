package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import io.varakh.eb.ecs.component.Direction
import io.varakh.eb.ecs.component.FacingComponent
import io.varakh.eb.ecs.component.PlayerComponent
import io.varakh.eb.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger
import kotlin.math.abs

private val log = logger<PlayerInputSystem>()

class PlayerInputSystem(private val viewport: Viewport)
    : IteratingSystem(
        allOf(PlayerComponent::class, TransformComponent::class, FacingComponent::class).get()) {

    private val facingVector = Vector2()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val facing = entity[FacingComponent.mapper]
        require(facing != null) { "Entity must have a FacingComponent. entity=$entity" }
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity must have a TransformComponent. entity=$entity" }

        facingVector.x = Gdx.input.x.toFloat()
        facingVector.y = Gdx.input.y.toFloat()
        viewport.unproject(facingVector)

        val xDiff = facingVector.x - transform.position.x - transform.size.x * 0.5f
        val yDiff = facingVector.y - transform.position.y - transform.size.y * 0.5f
        facing.direction = when {
            abs(xDiff) > abs(yDiff) -> if (xDiff > 0) Direction.RIGHT else Direction.LEFT
            else -> if (yDiff > 0) Direction.UP else Direction.DOWN
        }
    }
}