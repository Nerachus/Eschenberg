package io.varakh.eb

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import io.varakh.eb.asset.TextureAsset
import io.varakh.eb.asset.TextureAtlasAsset
import io.varakh.eb.audio.AudioService
import io.varakh.eb.audio.DefaultAudioService
import io.varakh.eb.ecs.system.*
import io.varakh.eb.screen.EschenbergScreen
import io.varakh.eb.screen.LoadingScreen
import ktx.app.KtxGame
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.debug
import ktx.log.logger

const val UNIT_SCALE = 1 / 16f
const val WORLD_WIDTH = 16f
const val WORLD_HEIGHT = 9f
const val BACKGROUND_WIDTH = 240f
const val BACKGROUND_HEIGHT = 135f

private val log = logger<Eschenberg>()

class Eschenberg : KtxGame<EschenbergScreen>() {

    val batch by lazy { SpriteBatch() }
    val gameViewport = FitViewport(WORLD_WIDTH, WORLD_HEIGHT)
    val pixelViewport = FitViewport(BACKGROUND_WIDTH, BACKGROUND_HEIGHT)
    val assets by lazy {
        KtxAsync.initiate()
        AssetStorage()
    }
    val audioService: AudioService by lazy { DefaultAudioService(assets) }

    val engine: PooledEngine by lazy {
        PooledEngine().apply {
            val atlas = assets[TextureAtlasAsset.GAME_GRAPHICS.descriptor]
            addSystem(PlayerInputSystem(gameViewport))
            addSystem(MoveSystem())
            addSystem(DamageSystem())
            addSystem(PowerUpSystem(audioService))
            addSystem(PlayerAnimationSystem(
                    regionUp = atlas.findRegion("HeroKnight_Idle", 0),
                    regionRight = atlas.findRegion("HeroKnight_Idle", 3),
                    regionDown = atlas.findRegion("HeroKnight_Idle", 5),
                    regionLeft = atlas.findRegion("HeroKnight_Idle", 7)
            ))
            addSystem(AttachSystem())
            addSystem(AnimationSystem(atlas))
            addSystem(CameraShakeSystem(gameViewport.camera))
            addSystem(RenderSystem(batch, gameViewport, pixelViewport, assets[TextureAsset.BACKGROUND.descriptor]))
            addSystem(RemoveSystem())
            addSystem(DebugSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        log.debug { "Create game instance" }
        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        super.dispose()
        log.debug { "Sprites in batch: ${batch.maxSpritesInBatch}" }
        batch.dispose()
        assets.dispose()
    }
}