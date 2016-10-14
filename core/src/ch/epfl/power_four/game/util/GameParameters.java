package ch.epfl.power_four.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameParameters {
    public static final String TAG = GameParameters.class.getName();
    public static final GameParameters instance = new GameParameters();
    private final Preferences prefs;


    private GameParameters() {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
        prefs.clear();
    }

    public void load() {

    }
}
