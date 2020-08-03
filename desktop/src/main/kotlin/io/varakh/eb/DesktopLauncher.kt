package io.varakh.eb

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main() {
    Lwjgl3Application(Eschenberg(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Eschenberg")
        setWindowedMode(16 * 64, 9 * 64)
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
    })
}