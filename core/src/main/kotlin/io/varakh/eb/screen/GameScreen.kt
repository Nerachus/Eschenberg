package io.varakh.eb.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.viewport.FitViewport
import io.varakh.eb.Eschenberg
import io.varakh.eb.UNIT_SCALE
import io.varakh.eb.ecs.component.GraphicComponent
import io.varakh.eb.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger

private val log = logger<GameScreen>()

class GameScreen(game: Eschenberg) : EschenbergScreen(game) {

    private val viewport = FitViewport(16f, 9f)
    private val playerTexture = Texture(Gdx.files.internal("graphics/ship_base.png"))
    private val player = engine.entity {
        with<TransformComponent> {
            position.set(1f, 1f, 0f)
        }
        with<GraphicComponent> {
            sprite.run {
                setRegion(playerTexture)
                setSize(texture.width * UNIT_SCALE, texture.height * UNIT_SCALE)
                setOriginCenter()
            }
        }
    }

    override fun show() {
        log.debug { "Game screen is shown." }
    }

    override fun render(delta: Float) {
        engine.update(delta)

        viewport.apply()
        batch.use(viewport.camera.combined) { batch ->
            player[GraphicComponent.mapper]?.let { graphic ->
                player[TransformComponent.mapper]?.let { transform ->
                    graphic.sprite.run {
                        rotation = transform.rotationDeg
                        setBounds(transform.position.x, transform.position.y,
                                transform.size.x, transform.size.y)
                        draw(batch)
                    }
                }
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        super.dispose()
        log.debug { "Sprites in batch: ${batch.maxSpritesInBatch}" }
        batch.dispose()
        playerTexture.dispose()
    }
}