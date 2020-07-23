package io.varakh.eb.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import io.varakh.eb.Eschenberg
import io.varakh.eb.ecs.component.FacingComponent
import io.varakh.eb.ecs.component.GraphicComponent
import io.varakh.eb.ecs.component.PlayerComponent
import io.varakh.eb.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger

private val log = logger<GameScreen>()

class GameScreen(game: Eschenberg) : EschenbergScreen(game) {

    private val playerTexture = Texture(Gdx.files.internal("graphics/ship_up.png"))
    private val player = engine.entity {
        with<TransformComponent> {
            position.set(3f, 3f, 0f)
        }
        with<GraphicComponent>()
        with<PlayerComponent>()
        with<FacingComponent>()
    }

    override fun show() {
        log.debug { "Game screen is shown." }
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