package io.varakh.eb.audio

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.utils.Pool
import io.varakh.eb.asset.MusicAsset
import io.varakh.eb.asset.SoundAsset
import kotlinx.coroutines.launch
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.debug
import ktx.log.error
import ktx.log.logger
import java.util.*
import kotlin.math.max

private val log = logger<AudioService>()

interface AudioService {
    fun play(soundAsset: SoundAsset, volume: Float = 1f)
    fun play(musicAsset: MusicAsset, volume: Float = 1f, loop: Boolean = true)
    fun pause()
    fun resume()
    fun stop(clearSounds: Boolean = true)
    fun update()
}

private class SoundRequest : Pool.Poolable {
    lateinit var soundAsset: SoundAsset
    var volume = 1f

    override fun reset() {
        volume = 1f
    }
}

private class SoundRequestPool : Pool<SoundRequest>() {
    override fun newObject() = SoundRequest()
}

class DefaultAudioService(private val assets: AssetStorage) : AudioService {

    private val soundRequestPool = SoundRequestPool()
    private val soundRequests = EnumMap<SoundAsset, SoundRequest>(SoundAsset::class.java)
    private var currentMusic: Music? = null
    private var currentMusicAsset: MusicAsset? = null

    override fun play(soundAsset: SoundAsset, volume: Float) {
        when {
            soundAsset in soundRequests -> soundRequests[soundAsset]?.let { it.volume = max(it.volume, volume) }
            soundRequests.size >= MAX_SOUND_INSTANCES ->
                log.debug { "Maximum sound requests already reached" }
            else -> {
                if (soundAsset.descriptor !in assets) {
                    log.error { "Trying to play a sound which is not loaded! $soundAsset" }
                }
                soundRequests[soundAsset] = soundRequestPool.obtain().apply {
                    this.soundAsset = soundAsset
                    this.volume = volume
                }
            }
        }
    }

    override fun play(musicAsset: MusicAsset, volume: Float, loop: Boolean) {
        val musicDeferred = assets.loadAsync(musicAsset.descriptor)
        KtxAsync.launch {
            musicDeferred.join()
            if (assets.isLoaded(musicAsset.descriptor)) { // wacky race condition check
                currentMusic?.stop()
                currentMusicAsset?.let { assets.unload(it.descriptor) }
                currentMusicAsset = musicAsset
                currentMusic = assets[musicAsset.descriptor].apply {
                    this.volume = volume
                    this.isLooping = loop
                    play()
                }
            }
        }
    }

    override fun pause() {
        currentMusic?.pause()
    }

    override fun resume() {
        currentMusic?.play()
    }

    override fun stop(clearSounds: Boolean) {
        currentMusic?.stop()
        if (clearSounds) soundRequests.clear()
    }

    override fun update() {
        if (soundRequests.isNotEmpty()) {
            soundRequests.values.forEach {
                assets[it.soundAsset.descriptor].play(it.volume)
                soundRequestPool.free(it)
            }
            soundRequests.clear()
        }
    }

    companion object {
        private const val MAX_SOUND_INSTANCES = 16
    }
}