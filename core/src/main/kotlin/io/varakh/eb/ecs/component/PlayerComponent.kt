package io.varakh.eb.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class PlayerComponent : Component, Pool.Poolable {

    var life = MAX_LIFE
    var shield = 0f
    var distance = 0f

    override fun reset() {
        life = MAX_LIFE
        shield = 0f
        distance = 0f
    }

    companion object {
        const val MAX_LIFE = 100f
        const val MAX_SHIELD = 100f
        val mapper = mapperFor<PlayerComponent>()
    }
}