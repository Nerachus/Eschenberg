package io.varakh.eb.screen

import com.badlogic.gdx.utils.viewport.Viewport
import io.varakh.eb.Eschenberg
import io.varakh.eb.audio.AudioService
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage

abstract class EschenbergScreen(val game: Eschenberg,
                                val gameViewport: Viewport = game.gameViewport,
                                val pixelViewport: Viewport = game.pixelViewport,
                                val assets: AssetStorage = game.assets,
                                val audioService: AudioService = game.audioService) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        pixelViewport.update(width, height, true)
        gameViewport.update(width, height, true)
    }
}