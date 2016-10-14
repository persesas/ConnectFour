package ch.epfl.power_four.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import ch.epfl.power_four.PowerFourMain;

public class DesktopLauncher {
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;


	public static void main (String[] arg) {
		int port = Integer.parseInt(arg[0]);

		if (rebuildAtlas) {
			TexturePacker.Settings settings = new TexturePacker.Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "assets-raw/images", "android/assets/images", "power_four.pack");
			TexturePacker.process(settings, "assets-raw/images-ui", "android/assets/images", "power_four-ui.pack");
			TexturePacker.process(settings, "assets-raw/images-connect-ui", "android/assets/images", "connect-ui.pack");
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) ch.epfl.power_four.game.util.Constants.VIEWPORT_GUI_WIDTH;
		config.height = (int) ch.epfl.power_four.game.util.Constants.VIEWPORT_GUI_HEIGHT;
		new LwjglApplication(new PowerFourMain(port), config);
	}
}
