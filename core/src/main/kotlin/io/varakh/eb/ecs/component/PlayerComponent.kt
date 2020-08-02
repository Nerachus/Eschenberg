package io.varakh.eb.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class PlayerComponent : Component, Pool.Poolable {

    var health = MAX_HEALTH
    var shield = 0f
    var points = 0

    override fun reset() {
        health = MAX_HEALTH
        shield = 0f
        points = 0
    }

    companion object {
        const val MAX_HEALTH = 100f
        const val MAX_SHIELD = 100f
        val mapper = mapperFor<PlayerComponent>()
    }
}