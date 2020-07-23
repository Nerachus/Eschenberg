package io.varakh.eb.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

enum class Direction { UP, RIGHT, DOWN, LEFT }

class FacingComponent : Component, Pool.Poolable {

    var direction = Direction.DOWN
    var lastDirection = Direction.DOWN

    override fun reset() {
        direction = Direction.DOWN
        lastDirection = Direction.DOWN
    }

    companion object {
        val mapper = mapperFor<FacingComponent>()
    }
}