package io.varakh.eb.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import io.varakh.eb.Eschenberg
import ktx.app.KtxScreen

abstract class EschenbergScreen(val game: Eschenberg,
                                val batch: SpriteBatch = game.batch,
                                val engine: PooledEngine = game.engine,
                                val gameViewport: Viewport = game.gameViewport,
                                val pixelViewport: Viewport = game.pixelViewport) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        pixelViewport.update(width, height, true)
        gameViewport.update(width, height, true)
    }
}