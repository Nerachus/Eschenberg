package io.varakh.eb.screen

import io.varakh.eb.Eschenberg
import io.varakh.eb.WORLD_WIDTH
import io.varakh.eb.ecs.component.*
import io.varakh.eb.ecs.system.DamageSystem
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
        with<AnimationComponent> { type = AnimationType.PLAYER_IDLE }
        with<GraphicComponent>()
        with<MoveComponent>()
        with<PlayerComponent>()
        with<FacingComponent>()
    }

    override fun show() {
        log.debug { "Game screen is shown." }
        engine.entity {
            with<TransformComponent> { size.set(WORLD_WIDTH, DamageSystem.DAMAGE_AREA_HEIGHT) }
            with<AnimationComponent> { type = AnimationType.DARK_MATTER }
            with<GraphicComponent>()
        }
    }

    override fun render(delta: Float) {
        engine.update(min(delta, MAX_FRAME_RATE))
    }
}