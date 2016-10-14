package ch.epfl.power_four;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import ch.epfl.power_four.game.screens.ConnectScreen;
import ch.epfl.power_four.game.util.Assets;
import ch.epfl.power_four.game.util.GameParameters;

public class PowerFourMain extends Game{
	public static int PORT;

	public PowerFourMain(int port){
		PORT = port;
	}

	@Override
	public void create () {
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		// Load preferences for audio settings and start playing music
		GameParameters.instance.load();
		// Start game at menu screen
		setScreen(new ConnectScreen(this));
	}
}
