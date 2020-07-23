package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.varakh.eb.ecs.component.Direction
import io.varakh.eb.ecs.component.FacingComponent
import io.varakh.eb.ecs.component.GraphicComponent
import io.varakh.eb.ecs.component.PlayerComponent
import ktx.ashley.allOf
import ktx.ashley.get

class PlayerAnimationSystem(private val regionUp: TextureRegion,
                            private val regionRight: TextureRegion,
                            private val regionDown: TextureRegion,
                            private val regionLeft: TextureRegion)
    : IteratingSystem(
        allOf(PlayerComponent::class, FacingComponent::class, GraphicComponent::class).get()),
        EntityListener {

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        val graphic = entity[GraphicComponent.mapper]!!
        graphic.setSpriteRegion(regionDown)
    }

    override fun entityRemoved(entity: Entity) = Unit

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val facing = entity[FacingComponent.mapper]!!
        val graphic = entity[GraphicComponent.mapper]
        require(graphic != null && graphic.sprite.texture != null) {
            "Entity must have a GraphicComponent with texture. entity=$entity"
        }

        if (facing.direction == facing.lastDirection) return

        facing.lastDirection = facing.direction
        graphic.setSpriteRegion(when (facing.direction) {
            Direction.UP -> regionUp
            Direction.RIGHT -> regionRight
            Direction.DOWN -> regionDown
            Direction.LEFT -> regionLeft
        })
    }
}