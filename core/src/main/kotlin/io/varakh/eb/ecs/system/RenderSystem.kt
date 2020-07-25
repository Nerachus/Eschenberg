package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
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
                   private val gameViewport: Viewport,
                   private val pixelViewport: Viewport,
                   backgroundTexture: Texture)
    : SortedIteratingSystem(
        allOf(TransformComponent::class, GraphicComponent::class).exclude(RemoveComponent::class).get(),
        compareBy { it[TransformComponent.mapper] }) {

    private val background = Sprite(backgroundTexture.apply {
        setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
    })
    private val backgroundScrollSpeed = Vector2(0.02f, -0.5f)

    override fun update(deltaTime: Float) {
        pixelViewport.apply()
        // render background
        batch.use(pixelViewport.camera.combined) {
            background.run {
                scroll(backgroundScrollSpeed.x * deltaTime, backgroundScrollSpeed.y * deltaTime)
                draw(batch)
            }
        }

        // render entities
        forceSort()
        gameViewport.apply()
        batch.use(gameViewport.camera.combined) {
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