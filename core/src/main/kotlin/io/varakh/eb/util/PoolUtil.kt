package io.varakh.eb.util

import com.badlogic.gdx.utils.Pool

inline fun <T> Pool<T>.pooled(block: (T) -> Unit) {
    val element = obtain()
    block(element)
    free(element)
}