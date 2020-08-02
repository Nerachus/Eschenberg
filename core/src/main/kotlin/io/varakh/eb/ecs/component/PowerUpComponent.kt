package io.varakh.eb.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import io.varakh.eb.asset.SoundAsset
import ktx.ashley.mapperFor

private const val BOOST_S_POINTS = 3f
private const val BOOST_L_POINTS = 5f
private const val HEALTH_GAIN = 25f
private const val SHIELD_GAIN = 25f

enum class PowerUpType(val animationType: AnimationType,
                       val soundAsset: SoundAsset,
                       val healthGain: Float = 0f,
                       val shieldGain: Float = 0f,
                       val pointsGain: Float = 0f) {
    NONE(AnimationType.NONE, SoundAsset.BOOST_L),
    BOOST_S(AnimationType.ORB_BLUE, SoundAsset.BOOST_S, pointsGain = BOOST_S_POINTS),
    BOOST_L(AnimationType.ORB_YELLOW, SoundAsset.BOOST_L, pointsGain = BOOST_L_POINTS),
    SHIELD(AnimationType.SHIELD, SoundAsset.SHIELD, shieldGain = SHIELD_GAIN),
    LIFE(AnimationType.LIFE, SoundAsset.LIFE, healthGain = HEALTH_GAIN);
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