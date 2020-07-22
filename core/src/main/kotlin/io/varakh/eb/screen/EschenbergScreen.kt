package io.varakh.eb.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import io.varakh.eb.Eschenberg
import ktx.app.KtxScreen

abstract class EschenbergScreen(val game: Eschenberg,
                                val batch: SpriteBatch = game.batch,
                                val engine: PooledEngine = game.engine,
                                val viewport: Viewport = game.viewport) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }
}