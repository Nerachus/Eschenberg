package io.varakh.eb.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import io.varakh.eb.Eschenberg
import io.varakh.eb.UNIT_SCALE
import ktx.log.debug
import ktx.log.logger

private val log = logger<MapScreen>()

class MapScreen(game: Eschenberg,
                map: TiledMap) : EschenbergScreen(game) {

    private val render = OrthogonalTiledMapRenderer(map, UNIT_SCALE)
    private val gameCam = OrthographicCamera()

    override fun show() {
        log.debug { "Map screen is shown." }
        gameViewport.camera = gameCam
        gameCam.position.set(gameViewport.worldWidth / 2f, gameViewport.worldHeight / 2f, 0f)
    }

    override fun render(delta: Float) {
        update(delta)
        render.render()
    }

    private fun update(delta: Float) {
        handleInput(delta)
        gameCam.update()
        render.setView(gameCam)
    }

    private fun handleInput(delta: Float) {
        when {
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) -> {
                gameCam.position.x += 10 * delta
            }
            Gdx.input.isKeyPressed(Input.Keys.LEFT) -> {
                gameCam.position.x -= 10 * delta
            }
            Gdx.input.isKeyPressed(Input.Keys.UP) -> {
                gameCam.position.y += 10 * delta
            }
            Gdx.input.isKeyPressed(Input.Keys.DOWN) -> {
                gameCam.position.y -= 10 * delta
            }
        }
    }
}