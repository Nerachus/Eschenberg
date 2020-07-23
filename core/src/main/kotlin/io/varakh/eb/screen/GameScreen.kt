package io.varakh.eb.screen

import io.varakh.eb.Eschenberg
import io.varakh.eb.ecs.component.*
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger

private val log = logger<GameScreen>()

class GameScreen(game: Eschenberg) : EschenbergScreen(game) {
    private val player = engine.entity {
        with<TransformComponent> {
            position.set(8f, 4.5f, 0f)
        }
        with<GraphicComponent>()
        with<MoveComponent>()
        with<PlayerComponent>()
        with<FacingComponent>()
    }

    override fun show() {
        log.debug { "Game screen is shown." }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }
}