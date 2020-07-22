package io.varakh.eb

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import io.varakh.eb.screen.LoadingScreen
import io.varakh.eb.screen.MenuScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger

private val log = logger<Eschenberg>()

class Eschenberg : KtxGame<KtxScreen>() {
    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        log.debug { "Create game instance" }
        addScreen(LoadingScreen(this))
        addScreen(MenuScreen(this))
        setScreen<LoadingScreen>()
    }
}