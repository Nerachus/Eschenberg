package io.varakh.eb

import com.badlogic.gdx.Game

/**
 * [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.
 */
class Eschenberg : Game() {
    override fun create() {
        setScreen(FirstScreen())
    }
}