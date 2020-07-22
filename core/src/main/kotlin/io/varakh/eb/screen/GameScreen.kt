package io.varakh.eb.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils.random
import io.varakh.eb.Eschenberg
import io.varakh.eb.UNIT_SCALE
import io.varakh.eb.ecs.component.GraphicComponent
import io.varakh.eb.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger

private val log = logger<GameScreen>()

class GameScreen(game: Eschenberg) : EschenbergScreen(game) {

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

        repeat(10) {
            engine.entity {
                with<TransformComponent> {
                    position.set(random(15f), random(8f), 0f)
                }
                with<GraphicComponent> {
                    sprite.run {
                        setRegion(playerTexture)
                        setSize(texture.width * UNIT_SCALE, texture.height * UNIT_SCALE)
                        setOriginCenter()
                    }
                }
            }
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun dispose() {
        super.dispose()
        log.debug { "Sprites in batch: ${batch.maxSpritesInBatch}" }
        batch.dispose()
        playerTexture.dispose()
    }
}