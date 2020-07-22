package io.varakh.eb.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import io.varakh.eb.Eschenberg
import ktx.log.debug
import ktx.log.logger


private val log = logger<LoadingScreen>()

class LoadingScreen(game: Eschenberg) : EschenbergScreen(game) {
    override fun show() {
        log.debug { "Loading screen is shown." }
    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
            game.setScreen<MenuScreen>()
    }
}
