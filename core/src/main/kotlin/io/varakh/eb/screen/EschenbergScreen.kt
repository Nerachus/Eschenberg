package io.varakh.eb.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.varakh.eb.Eschenberg
import ktx.app.KtxScreen

abstract class EschenbergScreen(val game: Eschenberg,
                                val batch: SpriteBatch = game.batch,
                                val engine: PooledEngine = game.engine) : KtxScreen