package io.varakh.eb.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

enum class PowerUpType(val animationType: AnimationType) {
    NONE(AnimationType.NONE),
    BOOST_S(AnimationType.ORB_BLUE),
    BOOST_L(AnimationType.ORB_YELLOW),
    SHIELD(AnimationType.SHIELD),
    LIFE(AnimationType.LIFE)
}

class PowerUpComponent : Component, Pool.Poolable {

    var type = PowerUpType.NONE

    override fun reset() {
        type = PowerUpType.NONE
    }

    companion object {
        val mapper = mapperFor<PowerUpComponent>()
    }
}