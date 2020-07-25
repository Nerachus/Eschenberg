package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import io.varakh.eb.ecs.component.Animation2D
import io.varakh.eb.ecs.component.AnimationComponent
import io.varakh.eb.ecs.component.AnimationType
import io.varakh.eb.ecs.component.GraphicComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.error
import ktx.log.logger
import java.util.*

private val log = logger<AnimationSystem>()

class AnimationSystem(
        private val atlas: TextureAtlas)
    : IteratingSystem(allOf(AnimationComponent::class, GraphicComponent::class).get()), EntityListener {

    private val animationCache = EnumMap<AnimationType, Animation2D>(AnimationType::class.java)

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        val anic = entity[AnimationComponent.mapper]!!
        anic.animation = getOrLoadAnimation(anic)
        val frame = anic.animation.getKeyFrame(anic.stateTime)
        entity[GraphicComponent.mapper]!!.setSpriteRegion(frame)
    }

    override fun entityRemoved(entity: Entity) = Unit


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val anic = entity[AnimationComponent.mapper]!!
        val graphic = entity[GraphicComponent.mapper]!!

        if (anic.type == AnimationType.NONE) {
            log.error { "No type specified for AnimationComponent $anic" }
            return
        }
        if (anic.type == anic.animation.type) {
            anic.stateTime += deltaTime
        } else {
            anic.stateTime = 0f
            anic.animation = getOrLoadAnimation(anic)
        }

        val frameSprite = anic.animation.getKeyFrame(anic.stateTime)
        graphic.setSpriteRegion(frameSprite)
    }

    private fun getOrLoadAnimation(anic: AnimationComponent) =
            animationCache[anic.type] ?: loadAnimation(anic.type)

    private fun loadAnimation(type: AnimationType): Animation2D {
        var regions = atlas.findRegions(type.atlasKey)
        if (regions.isEmpty) {
            log.error { "No regions found for ${type.atlasKey}" }
            regions = atlas.findRegions("error")
        }
        if (regions.isEmpty) throw IllegalArgumentException("No error region found in $atlas")
        log.debug { "Adding animation of type $type with ${regions.size}" }
        val animation = Animation2D(type, regions, type.mode, type.speedRate)
        animationCache[type] = animation
        return animation
    }
}