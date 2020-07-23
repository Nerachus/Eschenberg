package io.varakh.eb.screen

import io.varakh.eb.Eschenberg
import io.varakh.eb.ecs.component.*
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import kotlin.math.min

private val log = logger<GameScreen>()
private const val MAX_FRAME_RATE = 1 / 100f

class GameScreen(game: Eschenberg) : EschenbergScreen(game) {
    private val player = engine.entity {
        with<TransformComponent> {
            setInitialPosition(8f, 4.5f, 0f)
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
        engine.update(min(delta, MAX_FRAME_RATE))
    }
}