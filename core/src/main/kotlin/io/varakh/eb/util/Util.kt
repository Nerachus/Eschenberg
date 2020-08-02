package io.varakh.eb.util

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Pool
import ktx.graphics.use

inline fun <T> Pool<T>.pooled(block: (T) -> Unit) {
    val element = obtain()
    block(element)
    free(element)
}

inline fun <B : Batch> B.useWithShader(shader: ShaderProgram, projectionMatrix: Matrix4? = null, action: (B) -> Unit) {
    this.use(projectionMatrix) {
        this.shader = shader
        action(this)
        this.shader = null
    }
}