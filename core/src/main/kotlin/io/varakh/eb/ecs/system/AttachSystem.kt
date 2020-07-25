package io.varakh.eb.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import io.varakh.eb.ecs.component.AttachComponent
import io.varakh.eb.ecs.component.GraphicComponent
import io.varakh.eb.ecs.component.RemoveComponent
import io.varakh.eb.ecs.component.TransformComponent
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.get

class AttachSystem : EntityListener,
        IteratingSystem(allOf(AttachComponent::class, TransformComponent::class, GraphicComponent::class).get()) {

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) = Unit

    override fun entityRemoved(removedEntity: Entity) {
        entities.forEach {
            if (it[AttachComponent.mapper]?.attachEntity == removedEntity)
                it.addComponent<RemoveComponent>(engine)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val attach = entity[AttachComponent.mapper]!!
        val graphic = entity[GraphicComponent.mapper]!!
        val transform = entity[TransformComponent.mapper]!!

        attach.attachEntity[TransformComponent.mapper]?.let {
            transform.interpolatedPosition.set(
                    it.interpolatedPosition.x + attach.offset.x,
                    it.interpolatedPosition.y + attach.offset.y,
                    transform.position.z)
        }

        attach.attachEntity[GraphicComponent.mapper]?.let {
            graphic.sprite.setAlpha(it.sprite.color.a)
        }
    }
}