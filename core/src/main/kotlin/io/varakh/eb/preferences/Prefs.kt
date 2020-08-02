package io.varakh.eb.preferences

import com.badlogic.gdx.Preferences
import ktx.preferences.get

enum class Prefs(val key: String) {
    HIGHSCORE("highscore")
}

inline operator fun <reified T> Preferences.get(key: Prefs): T? = this[key.key]

inline operator fun <reified T> Preferences.get(key: Prefs, defaultValue: T): T =
        if (key.key in this) this[key.key]!! else defaultValue