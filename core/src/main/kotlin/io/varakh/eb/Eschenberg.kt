package io.varakh.eb

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.FitViewport
import io.varakh.eb.ecs.system.*
import io.varakh.eb.screen.EschenbergScreen
import io.varakh.eb.screen.GameScreen
import io.varakh.eb.screen.LoadingScreen
import io.varakh.eb.screen.MenuScreen
import ktx.app.KtxGame
import ktx.log.debug
import ktx.log.logger

const val UNIT_SCALE = 1 / 16f
const val WORLD_WIDTH = 16f
const val WORLD_HEIGHT = 9f
const val BACKGROUND_WIDTH = 240f
const val BACKGROUND_HEIGHT = 135f

private val log = logger<Eschenberg>()

class Eschenberg : KtxGame<EschenbergScreen>() {

    private val graphicsAtlas by lazy { TextureAtlas(Gdx.files.internal("graphics/graphics.atlas")) }
    private val backgroundTexture by lazy { Texture(Gdx.files.internal("graphics/background.png")) }

    val batch by lazy { SpriteBatch() }
    val gameViewport = FitViewport(WORLD_WIDTH, WORLD_HEIGHT)
    val pixelViewport = FitViewport(BACKGROUND_WIDTH, BACKGROUND_HEIGHT)
    val engine: PooledEngine by lazy {
        PooledEngine().apply {
            addSystem(PlayerInputSystem(gameViewport))
            addSystem(MoveSystem())
            addSystem(DamageSystem())
            addSystem(PowerUpSystem())
            addSystem(PlayerAnimationSystem(
                    regionUp = graphicsAtlas.findRegion("HeroKnight_Idle", 0),
                    regionRight = graphicsAtlas.findRegion("HeroKnight_Idle", 3),
                    regionDown = graphicsAtlas.findRegion("HeroKnight_Idle", 5),
                    regionLeft = graphicsAtlas.findRegion("HeroKnight_Idle", 7)
            ))
            addSystem(AttachSystem())
            addSystem(AnimationSystem(graphicsAtlas))
            addSystem(CameraShakeSystem(gameViewport.camera))
            addSystem(RenderSystem(batch, gameViewport, pixelViewport, backgroundTexture))
            addSystem(RemoveSystem())
            addSystem(DebugSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        log.debug { "Create game instance" }
        addScreen(LoadingScreen(this))
        addScreen(MenuScreen(this))
        addScreen(GameScreen(this))
        setScreen<GameScreen>()
    }

    override fun dispose() {
        super.dispose()
        log.debug { "Sprites in batch: ${batch.maxSpritesInBatch}" }
        batch.dispose()
        graphicsAtlas.dispose()
        backgroundTexture.dispose()
    }
}