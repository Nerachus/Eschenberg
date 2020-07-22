package io.varakh.eb;

import com.badlogic.gdx.Game;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Eschenberg extends Game {
    @Override
    public void create() {
        setScreen(new FirstScreen());
    }
}