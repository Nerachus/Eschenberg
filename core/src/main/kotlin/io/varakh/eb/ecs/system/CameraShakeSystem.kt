package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import io.varakh.eb.event.GameEventListener
import io.varakh.eb.event.GameEventManagers
import io.varakh.eb.event.PlayerDamageEvent
import ktx.collections.GdxArray

private class CameraShake : Pool.Poolable {
    var duration = 0f
    var maxDistortion = 0f
    lateinit var camera: Camera
    private var storeCameraPos = true
    private val oldCameraPosition = Vector3()
    private var currentDuration = 0f

    override fun reset() {
        duration = 0f
        maxDistortion = 0f
        storeCameraPos = true
        oldCameraPosition.set(Vector3.Zero)
        currentDuration = 0f
    }

    fun update(deltaTime: Float): Boolean {
        if (storeCameraPos) {
            storeCameraPos = false
            oldCameraPosition.set(camera.position)
        }
        if (currentDuration < duration) {
            val currentPower = maxDistortion * ((duration - currentDuration) / duration)
            camera.position.x = oldCameraPosition.x + random(-1f, 1f) * currentPower
            camera.position.y = oldCameraPosition.y + random(-1f, 1f) * currentPower
            camera.update()

            currentDuration += deltaTime
            return false
        }
        camera.position.set(oldCameraPosition)
        camera.update()
        return true
    }
}

private class CameraShakePool(private val gameCamera: Camera) : Pool<CameraShake>() {
    override fun newObject() = CameraShake().apply {
        this.camera = gameCamera
    }
}


class CameraShakeSystem(camera: Camera) : EntitySystem(), GameEventListener<PlayerDamageEvent> {

    private val playerDamageManager = GameEventManagers[PlayerDamageEvent::class]
    private val shakePool = CameraShakePool(camera)
    private val activeShakes = GdxArray<CameraShake>()

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        playerDamageManager.addEventListener(this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        playerDamageManager.removeEventListener(this)
    }

    override fun onEvent(event: PlayerDamageEvent) {
        if (activeShakes.size < SHAKES_PER_HIT) {
            activeShakes.add(shakePool.obtain().apply {
                duration = 0.25f
                maxDistortion = 0.25f
            })
        }
    }

    override fun update(deltaTime: Float) {
        if (activeShakes.isEmpty) return
        val shake = activeShakes.first()
        if (shake.update(deltaTime)) {
            activeShakes.removeIndex(0)
            shakePool.free(shake)
        }
    }

    companion object {
        private const val SHAKES_PER_HIT = 2
    }
}
