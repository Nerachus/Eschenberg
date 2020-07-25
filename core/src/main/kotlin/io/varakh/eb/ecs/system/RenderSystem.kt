package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import io.varakh.eb.ecs.component.GraphicComponent
import io.varakh.eb.ecs.component.RemoveComponent
import io.varakh.eb.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.logger

private val log = logger<RenderSystem>()

class RenderSystem(private val batch: SpriteBatch,
                   private val viewport: Viewport)
    : SortedIteratingSystem(
        allOf(TransformComponent::class, GraphicComponent::class).exclude(RemoveComponent::class).get(),
        compareBy { it[TransformComponent.mapper] }) {

    override fun update(deltaTime: Float) {
        forceSort()
        viewport.apply()
        batch.use(viewport.camera.combined) {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]!!
        val graphic = entity[GraphicComponent.mapper]
        require(graphic != null && graphic.sprite.texture != null) {
            "Entity must have a GraphicComponent with texture. entity=$entity"
        }

        graphic.sprite.run {
            rotation = transform.rotationDeg
            setBounds(transform.interpolatedPosition.x, transform.interpolatedPosition.y,
                    transform.size.x, transform.size.y)
            draw(batch)
        }
    }
}