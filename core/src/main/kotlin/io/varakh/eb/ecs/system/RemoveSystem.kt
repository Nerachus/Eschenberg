package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import io.varakh.eb.ecs.component.RemoveComponent
import ktx.ashley.allOf
import ktx.ashley.get

class RemoveSystem : IteratingSystem(allOf(RemoveComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val removed = entity[RemoveComponent.mapper]!!

        removed.delay -= deltaTime
        if (removed.delay <= 0f) engine.removeEntity(entity)
    }
}