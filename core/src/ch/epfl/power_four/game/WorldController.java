package ch.epfl.power_four.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

import ch.epfl.power_four.game.network.BroadcasterMediator;
import ch.epfl.power_four.game.network.Player;
import ch.epfl.power_four.game.objects.AbstractGameObject;
import ch.epfl.power_four.game.objects.Coin;
import ch.epfl.power_four.game.objects.Grid;
import ch.epfl.power_four.game.objects.GridManager;
import ch.epfl.power_four.game.util.CameraHelper;
import ch.epfl.power_four.game.util.Constants;

public class WorldController implements InputProcessor{
    public boolean isGameFinished;
    public boolean connected;
    public boolean waitingAck;
    public int helpCol = -1;
    public World world;
    public CameraHelper cameraHelper;
    public Game game;
    public ArrayList<AbstractGameObject> objects;
    public GridManager gridManager;
    public Player otherPlayer;
    public Timer timer;
    public Timer.Task task;
    private int playedCol;


    public WorldController(){
        init();
    }

    private void init() {
        world = new World(new Vector2(0f, -10f), true);
        objects = new ArrayList<AbstractGameObject>();
        cameraHelper = new CameraHelper();
        isGameFinished = false;
        waitingAck = false;
        connected = false;

        timer = new Timer();

        Grid g = new Grid();
        g.position.set(-g.dimension.x/2,-g.dimension.y/2);
        g.initPhysics(world);

        cameraHelper.setTarget(g);

        objects.add(g);
        gridManager = new GridManager(10, 7);
    }

    public void activate(){
        Gdx.input.setInputProcessor(this);
    }

    public void clearGame(){
        // Clear everything
        gridManager.clearGame();
        objects.clear();
        // Create new Game
        world = new World(new Vector2(0f, -10f), true);
        Grid g = new Grid();
        g.position.set(-g.dimension.x/2,-g.dimension.y/2);
        g.initPhysics(world);
        objects.add(g);
        isGameFinished = false;

    }

    public void update(float deltaTime){
        cameraHelper.update(deltaTime);
        for (AbstractGameObject o: objects){
            o.update(deltaTime);
        }
        world.step(deltaTime, 8, 3);
    }

    public void render(SpriteBatch batch){
        for (AbstractGameObject o: objects){
            o.render(batch);
        }
    }

    public int playCol(int i, boolean forceMove){
        if(forceMove || gridManager.player != otherPlayer.name ) {
            if (gridManager.addElem(i) != -1) {
                Coin c = new Coin(gridManager.player);
                float COL_0 = -1.95f;
                float OFF = 0.57f;
                c.position.set(COL_0 + OFF * i, Constants.VIEWPORT_WIDTH / 2);
                c.initPhysics(world);
                objects.add(c);
                isGameFinished = gridManager.hasWon() || gridManager.isTie();
                if (!isGameFinished) gridManager.nextPlayer();
                return 0;
            }
        }
        return -1;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(connected) {
            BroadcasterMediator bm = new BroadcasterMediator(this);
            if (!isGameFinished) {
                int width = 56;
                float offset = (WorldRenderer.cameraGUI.viewportWidth-397)/2;
                playedCol = (int) ((screenX - offset) / width);
                if (playedCol > 6) playedCol = 6;
                if (playCol(playedCol, false) >= 0) {
                    bm.play_col_y(playedCol);

                    task = new Timer.Task() {
                        @Override
                        public void run() {
                            BroadcasterMediator bm = new BroadcasterMediator(WorldController.this);
                            bm.play_col_y(playedCol);
                        }
                    };
                    timer.scheduleTask(task, 2);
                }

            } else {
                clearGame();
                bm.clear_game();
            }
            helpCol = -1;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(!isGameFinished) {
            int width = 56;
            float offset = (WorldRenderer.cameraGUI.viewportWidth-397)/2;
            int playedCol = (int) ((screenX - offset) / width);
            if (playedCol > 6) playedCol = 6;
            helpCol = playedCol;
        }
        return false;
    }

    private Grid getGrid(){
        for (AbstractGameObject o: objects){
            if(o instanceof Grid) return (Grid) o;
        }
        return null;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
