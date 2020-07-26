package io.varakh.eb.screen

import com.badlogic.ashley.core.Entity
import io.varakh.eb.Eschenberg
import io.varakh.eb.UNIT_SCALE
import io.varakh.eb.WORLD_WIDTH
import io.varakh.eb.ecs.component.*
import io.varakh.eb.ecs.system.DamageSystem
import io.varakh.eb.event.GameEventListener
import io.varakh.eb.event.GameEventManagers
import io.varakh.eb.event.GameEventPlayerDeath
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import kotlin.math.min

private val log = logger<GameScreen>()
private const val MAX_FRAME_RATE = 1 / 100f

class GameScreen(game: Eschenberg) : EschenbergScreen(game), GameEventListener<GameEventPlayerDeath> {

    private val playerDeathManager = GameEventManagers[GameEventPlayerDeath::class]

    override fun show() {
        log.debug { "Game screen is shown." }
        playerDeathManager.addEventListener(this)
        spawnPlayer()
        engine.entity {
            with<TransformComponent> { size.set(WORLD_WIDTH, DamageSystem.DAMAGE_AREA_HEIGHT) }
            with<AnimationComponent> { type = AnimationType.DARK_MATTER }
            with<GraphicComponent>()
        }
    }

    override fun hide() {
        super.dispose()
        playerDeathManager.removeEventListener(this)
    }

    override fun render(delta: Float) {
        engine.update(min(delta, MAX_FRAME_RATE))
    }

    override fun onEvent(event: GameEventPlayerDeath) {
        spawnPlayer()
    }

    private fun spawnPlayer(): Entity {
        val player = engine.entity {
            with<TransformComponent> {
                setInitialPosition(8f, 4.5f, 2f)
            }
            with<AnimationComponent> { type = AnimationType.PLAYER_IDLE }
            with<GraphicComponent>()
            with<MoveComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }
        engine.entity {
            with<AttachComponent> {
                attachEntity = player
                offset.set(4f * UNIT_SCALE, -5f * UNIT_SCALE)
            }
            with<TransformComponent> {
                size.set(0.5f, 0.5f)
                setInitialPosition(0f, 0f, 1f) // x/y don't matter, be behind player but before background
            }
            with<AnimationComponent> { type = AnimationType.FIRE }
            with<GraphicComponent>()
        }
        return player
    }
}