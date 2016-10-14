package ch.epfl.power_four.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ch.epfl.power_four.PowerFourMain;
import ch.epfl.power_four.game.WorldController;
import ch.epfl.power_four.game.WorldRenderer;
import ch.epfl.power_four.game.network.BroadcasterMediator;
import ch.epfl.power_four.game.network.Player;
import ch.epfl.power_four.game.network.Server;

public class GameScreen extends AbstractGameScreen {
    private static final String TAG = GameScreen.class.getName();
    private WorldController worldController;
    private WorldRenderer worldRenderer;
    private boolean paused;

    public GameScreen(Game game, WorldController worldController) {
        super(game);
        this.worldController = worldController;
    }


    @Override
    public void render(float deltaTime) {
        // Do not update game world when paused.
        if (!paused) {
            // Update game world by the time that has passed
            // since last rendered frame.
            worldController.update(deltaTime);
        }
        Gdx.gl.glClearColor(.6f,.6f,.6f,1);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Render game world to screen
        worldRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
    }

    @Override
    public void show() {
        worldRenderer = new WorldRenderer(worldController);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void hide() {
        worldRenderer.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume(){
        super.resume();
        paused = false;
    }
}
