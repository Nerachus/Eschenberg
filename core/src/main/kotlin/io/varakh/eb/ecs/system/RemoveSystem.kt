package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import io.varakh.eb.ecs.component.RemovedComponent
import ktx.ashley.allOf
import ktx.ashley.get

class RemoveSystem : IteratingSystem(allOf(RemovedComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val removed = entity[RemovedComponent.mapper]
        require(removed != null) { "Entity must have a RemovedComponent. entity=$entity" }

        removed.delay -= deltaTime
        if (removed.delay <= 0f) engine.removeEntity(entity)
    }
}