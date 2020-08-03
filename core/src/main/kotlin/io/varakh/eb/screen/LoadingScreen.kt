package io.varakh.eb.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import io.varakh.eb.Eschenberg
import io.varakh.eb.asset.ShaderProgramAsset
import io.varakh.eb.asset.SoundAsset
import io.varakh.eb.asset.TextureAsset
import io.varakh.eb.asset.TextureAtlasAsset
import io.varakh.eb.ui.LabelStyles
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger
import ktx.scene2d.*

private val log = logger<LoadingScreen>()

class LoadingScreen(game: Eschenberg) : EschenbergScreen(game) {

    private lateinit var progressBar: Image
    private lateinit var touchToBeginLabel: Label

    override fun show() {
        val start = System.currentTimeMillis()
        log.debug { "Loading screen is shown." }
        val assetRefs = gdxArrayOf(
                TextureAsset.values().map { assets.loadAsync(it.descriptor) },
                TextureAtlasAsset.values().map { assets.loadAsync(it.descriptor) },
                SoundAsset.values().map { assets.loadAsync(it.descriptor) },
                ShaderProgramAsset.values().map { assets.loadAsync(it.descriptor) }
        ).flatten()

        KtxAsync.launch {
            assetRefs.joinAll()
            assetsLoaded()
            log.debug { "Time for loading assets: ${System.currentTimeMillis() - start}ms" }
        }

        setupUI()
    }

    override fun hide() {
        stage.clear()
    }

    override fun render(delta: Float) {
        if (assets.progress.isFinished && Gdx.input.justTouched() && game.containsScreen<GameScreen>()) {
            game.setScreen<GameScreen>()
            dispose()
        }

        progressBar.scaleX = assets.progress.percent
        stage.run {
            viewport.apply()
            act()
            draw()
        }
    }

    private fun assetsLoaded() {
        game.addScreen(GameScreen(game))
        game.addScreen(MenuScreen(game))
        touchToBeginLabel += forever(sequence(fadeIn(0.5f) + fadeOut(0.5f)))
    }

    private fun setupUI() {
        stage.actors {
            table {
                defaults().fillX().expandX()
                label("Eschenberg", LabelStyles.GRADIENT.name) {
                    wrap = true
                    setAlignment(Align.center)
                }
                row()

                touchToBeginLabel = label("Touch To Begin", LabelStyles.DEFAULT.name) {
                    wrap = true
                    setAlignment(Align.center)
                    color.a = 0f
                }
                row()

                stack { cell ->
                    progressBar = image("life_bar") {
                        scaleX = 0f
                    }
                    label("Loading...", LabelStyles.DEFAULT.name) {
                        setAlignment(Align.center)
                    }
                    cell.padLeft(5f).padRight(5f)
                }

                setFillParent(true)
                pack()
            }
        }
        stage.isDebugAll = false
    }
}
