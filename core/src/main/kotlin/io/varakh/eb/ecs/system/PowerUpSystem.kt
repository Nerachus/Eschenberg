package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.Rectangle
import io.varakh.eb.WORLD_HEIGHT
import io.varakh.eb.WORLD_WIDTH
import io.varakh.eb.ecs.component.*
import io.varakh.eb.event.CollectPowerUpEvent
import io.varakh.eb.event.GameEventManagers
import ktx.ashley.*
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger
import kotlin.math.min

private val log = logger<PowerUpSystem>()

private class SpawnPattern(
        type1: PowerUpType = PowerUpType.NONE,
        type2: PowerUpType = PowerUpType.NONE,
        type3: PowerUpType = PowerUpType.NONE,
        type4: PowerUpType = PowerUpType.NONE,
        type5: PowerUpType = PowerUpType.NONE,
        val types: GdxArray<PowerUpType> = gdxArrayOf(type1, type2, type3, type4, type5)
)

class PowerUpSystem : IteratingSystem(allOf(PowerUpComponent::class, TransformComponent::class)
        .exclude(RemoveComponent::class.java).get()) {

    private val playerBoundsRect = Rectangle()
    private val powerUpBoundsRect = Rectangle()
    private val playerEntities by lazy {
        engine.getEntitiesFor(allOf(PlayerComponent::class).exclude(RemoveComponent::class).get())
    }
    private val spawnPatterns = gdxArrayOf(
            SpawnPattern(type1 = PowerUpType.BOOST_S, type2 = PowerUpType.BOOST_L, type5 = PowerUpType.LIFE),
            SpawnPattern(type2 = PowerUpType.LIFE, type3 = PowerUpType.SHIELD, type4 = PowerUpType.BOOST_L)
    )
    private var spawnTime = 0f
    private val currentSpawnPattern = GdxArray<PowerUpType>()
    private val powerUpEventManager = GameEventManagers[CollectPowerUpEvent::class]

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        spawnTime -= deltaTime
        if (spawnTime <= 0f) {
            spawnTime = random(MIN_SPAWN_INTERVAL, MAX_SPAWN_INTERVAL)
            if (currentSpawnPattern.isEmpty) {
                currentSpawnPattern.addAll(spawnPatterns[random(0, spawnPatterns.size - 1)].types)
                log.debug { "Next pattern: $currentSpawnPattern" }
            }

            val powerUpType = currentSpawnPattern.removeIndex(0)
            if (powerUpType == PowerUpType.NONE) return
            spawnPowerUp(powerUpType, 1f + random(0, WORLD_WIDTH.toInt() - 1), WORLD_HEIGHT)
        }
    }

    private fun spawnPowerUp(powerUpType: PowerUpType, x: Float, y: Float) {
        engine.entity {
            with<PowerUpComponent> { type = powerUpType }
            with<TransformComponent> { setInitialPosition(x, y, 0f) }
            with<AnimationComponent> { type = powerUpType.animationType }
            with<GraphicComponent>()
            with<MoveComponent> { speed.y = POWER_UP_SPEED }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]!!
        if (transform.position.y <= 1f) {
            entity.addComponent<RemoveComponent>(engine)
            return
        }

        powerUpBoundsRect.set(transform.position.x, transform.position.y,
                transform.size.x, transform.size.y)
        playerEntities.forEach { player ->
            player[TransformComponent.mapper]?.let {
                playerBoundsRect.set(it.position.x, it.position.y,
                        it.size.x, it.size.y)
                if (playerBoundsRect.overlaps(powerUpBoundsRect)) {
                    collectPowerUp(player, entity)
                }
            }
        }
    }

    private fun collectPowerUp(entity: Entity, powerUp: Entity) {
        val playerComp = entity[PlayerComponent.mapper]!!
        val powerUpType = powerUp[PowerUpComponent.mapper]!!.type

        playerComp.points += powerUpType.pointsGain
        playerComp.health = min(playerComp.health + powerUpType.healthGain, PlayerComponent.MAX_HEALTH)
        playerComp.shield = min(playerComp.shield + powerUpType.shieldGain, PlayerComponent.MAX_SHIELD)

        powerUpEventManager.dispatchEvent {
            player = entity
            type = powerUpType
        }
        powerUp.addComponent<RemoveComponent>(engine)
    }

    companion object {
        const val MAX_SPAWN_INTERVAL = 1.5f
        const val MIN_SPAWN_INTERVAL = 0.9f
        const val POWER_UP_SPEED = -2.75f
    }
}