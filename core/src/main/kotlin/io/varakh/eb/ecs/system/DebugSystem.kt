package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import io.varakh.eb.ecs.component.PlayerComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger

private val log = logger<DebugSystem>()

class DebugSystem : IntervalIteratingSystem(allOf(PlayerComponent::class).get(), WINDOW_INFO_UPDATE_RATE) {

    init {
        setProcessing(true)
    }

    override fun processEntity(entity: Entity) {
        val player = entity[PlayerComponent.mapper]!!

        Gdx.graphics.setTitle(
                "Player Health = ${player.health} - Player Shield = ${player.shield} - Points = ${player.points}")

        when {
            Gdx.input.isKeyPressed(Input.Keys.NUMPAD_1) -> {
                player.health = 0f
                player.shield = 0f
            }
            Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2) -> player.shield = PlayerComponent.MAX_SHIELD
        }
    }

    companion object {
        private const val WINDOW_INFO_UPDATE_RATE = 0.25f
    }
}