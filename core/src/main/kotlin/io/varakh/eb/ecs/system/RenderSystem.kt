package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.MathUtils.clamp
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import io.varakh.eb.ecs.component.*
import io.varakh.eb.event.CollectPowerUpEvent
import io.varakh.eb.event.GameEventListener
import io.varakh.eb.event.GameEventManagers
import io.varakh.eb.util.useWithShader
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.graphics.use
import kotlin.math.min

class RenderSystem(private val batch: SpriteBatch,
                   private val gameViewport: Viewport,
                   private val pixelViewport: Viewport,
                   private val outlineShader: ShaderProgram,
                   backgroundTexture: Texture) : GameEventListener<CollectPowerUpEvent>, SortedIteratingSystem(
        allOf(TransformComponent::class, GraphicComponent::class).exclude(RemoveComponent::class).get(),
        compareBy { it[TransformComponent.mapper] }) {

    private val powerUpEventManager = GameEventManagers[CollectPowerUpEvent::class]
    private val backgroundScrollSpeed = Vector2(0.02f, BACKGROUND_Y_SCROLL)
    private val textureSizeLocation = outlineShader.getUniformLocation("u_textureSize")
    private val outlineColorLoc = outlineShader.getUniformLocation("u_outlineColor")
    private val outlineColor = Color.SKY
    private val background = Sprite(backgroundTexture.apply {
        setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
    })
    private val playerEntities by lazy {
        engine.getEntitiesFor(allOf(PlayerComponent::class).exclude(RemoveComponent::class).get())
    }

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        powerUpEventManager.addEventListener(this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        powerUpEventManager.removeEventListener(this)
    }

    override fun update(deltaTime: Float) {
        pixelViewport.apply()
        // render background
        batch.use(pixelViewport.camera.combined) {
            background.run {
                // go back to normal speed over 20 seconds
                backgroundScrollSpeed.y = min(BACKGROUND_Y_SCROLL, backgroundScrollSpeed.y + deltaTime * (1 / 20f))
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

        // render outlines of entities
        renderEntityOutline()
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

    override fun onEvent(event: CollectPowerUpEvent) {
        when (event.type) {
            PowerUpType.BOOST_S -> backgroundScrollSpeed.y -= 0.1f
            PowerUpType.BOOST_L -> backgroundScrollSpeed.y -= 0.2f
            else -> Unit
        }
    }

    private fun renderEntityOutline() {
        batch.useWithShader(outlineShader, gameViewport.camera.combined) {
            playerEntities.forEach { entity ->
                renderPlayerOutline(entity, it)
            }
        }
    }

    private fun renderPlayerOutline(entity: Entity, batch: SpriteBatch) {
        val player = entity[PlayerComponent.mapper]!!
        val graphic = entity[GraphicComponent.mapper]!!
        if (player.shield > 0f) {
            outlineColor.a = clamp(player.shield / PlayerComponent.MAX_SHIELD, 0f, 1f)
            outlineShader.setUniformf(outlineColorLoc, outlineColor)
            graphic.sprite.run {
                outlineShader.setUniformf(textureSizeLocation, texture.width.toFloat(), texture.height.toFloat())
                draw(batch)
            }
        }
    }

    companion object {
        private const val BACKGROUND_Y_SCROLL = -0.2f
    }
}