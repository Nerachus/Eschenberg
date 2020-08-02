package io.varakh.eb.screen

import io.varakh.eb.Eschenberg
import io.varakh.eb.asset.TextureAsset
import io.varakh.eb.asset.TextureAtlasAsset
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger

private val log = logger<LoadingScreen>()

class LoadingScreen(game: Eschenberg) : EschenbergScreen(game) {
    override fun show() {
        val start = System.currentTimeMillis()
        log.debug { "Loading screen is shown." }
        val assetRefs = gdxArrayOf(
                TextureAsset.values().map { assets.loadAsync(it.descriptor) },
                TextureAtlasAsset.values().map { assets.loadAsync(it.descriptor) }
        ).flatten()

        KtxAsync.launch {
            assetRefs.joinAll()
            assetsLoaded()
        }
        log.debug { "Time for loading assets: ${System.currentTimeMillis() - start}ms" }
    }

    private fun assetsLoaded() {
        game.addScreen(MenuScreen(game))
        game.addScreen(GameScreen(game))
        game.setScreen<GameScreen>()
        dispose()
    }
}
