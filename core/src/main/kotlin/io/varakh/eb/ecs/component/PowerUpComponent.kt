package io.varakh.eb.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

private const val BOOST_S_POINTS = 3f
private const val BOOST_L_POINTS = 5f
private const val HEALTH_GAIN = 25f
private const val SHIELD_GAIN = 25f

enum class PowerUpType(val animationType: AnimationType,
                       val healthGain: Float = 0f,
                       val shieldGain: Float = 0f,
                       val pointsGain: Float = 0f) {
    NONE(AnimationType.NONE),
    BOOST_S(AnimationType.ORB_BLUE, pointsGain = BOOST_S_POINTS),
    BOOST_L(AnimationType.ORB_YELLOW, pointsGain = BOOST_L_POINTS),
    SHIELD(AnimationType.SHIELD, shieldGain = SHIELD_GAIN),
    LIFE(AnimationType.LIFE, healthGain = HEALTH_GAIN);
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