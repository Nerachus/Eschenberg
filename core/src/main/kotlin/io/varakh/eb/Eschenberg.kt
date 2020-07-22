package io.varakh.eb

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import io.varakh.eb.ecs.system.RenderSystem
import io.varakh.eb.screen.EschenbergScreen
import io.varakh.eb.screen.GameScreen
import io.varakh.eb.screen.LoadingScreen
import io.varakh.eb.screen.MenuScreen
import ktx.app.KtxGame
import ktx.log.debug
import ktx.log.logger

const val UNIT_SCALE = 1 / 16f
private val log = logger<Eschenberg>()

class Eschenberg : KtxGame<EschenbergScreen>() {

    val batch by lazy { SpriteBatch() }
    val viewport = FitViewport(16f, 9f)
    val engine: PooledEngine by lazy {
        PooledEngine().apply { addSystem(RenderSystem(batch, viewport)) }
    }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        log.debug { "Create game instance" }
        addScreen(LoadingScreen(this))
        addScreen(MenuScreen(this))
        addScreen(GameScreen(this))
        setScreen<GameScreen>()
    }
}