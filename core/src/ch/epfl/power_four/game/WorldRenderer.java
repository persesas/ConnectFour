package ch.epfl.power_four.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;

import ch.epfl.power_four.game.util.Assets;
import ch.epfl.power_four.game.util.Constants;

public class WorldRenderer implements Disposable{
    private WorldController worldController;
    private SpriteBatch batch;
    private static OrthographicCamera camera;      // Camera for the Game world
    static OrthographicCamera cameraGUI;      // Camera for UI
    // private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();    // Shows, bodies fixtures

    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    private void init() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(Constants.VIEWPORT_WIDTH / 2, (Constants.VIEWPORT_GUI_HEIGHT / 2), 0);
        camera.update();

        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(Constants.VIEWPORT_GUI_WIDTH / 2, Constants.VIEWPORT_GUI_HEIGHT / 2, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void render() {
        if(worldController.connected) renderWorld(batch);
        renderGUI(batch);
        //debugRenderer.render(worldController.world, camera.combined);
    }


    private void renderWorld(SpriteBatch batch) {
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.render(batch);
        batch.end();
    }

    private void renderGUI(SpriteBatch batch) {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        if(!worldController.connected) renderWaitingPlayer(batch);
        else {
            renderPlayerInfo(batch);
            if (worldController.helpCol != -1) renderHelpCol(batch);
            if (worldController.gridManager.hasWon())
                renderWinningCoins(batch);
            else if(worldController.gridManager.isTie()){
                renderTie(batch);
            }
        }
        batch.end();
    }

    private void renderTie(SpriteBatch batch) {
        TextureAtlas.AtlasRegion textureRegion = Assets.instance.playerUI.tie;
        float img_ratio = (float) textureRegion.getRegionWidth() / (float) textureRegion.getRegionHeight();
        Vector2 dimension = new Vector2(img_ratio, 1f);
        Vector2 scale = new Vector2(50,50);
        Vector2 origin = new Vector2(dimension.x/2, dimension.y/2);
        float x = dimension.cpy().scl(scale).x/2;
        float y = 30;

        // draw image
        batch.draw(textureRegion.getTexture(), x, y+15, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                0, textureRegion.getRegionX(), textureRegion.getRegionY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight(),
                false, true);
    }

    private void renderWinningCoins(SpriteBatch batch){
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 1f, 0f, 1f);
        pm.fillCircle(0,0,10);
        TextureRegion tr = new TextureRegion(new Texture(pm));
        int dimX = 56;
        int dimY = 51;
        float offsetX = (cameraGUI.viewportWidth-397)/2 + dimX/2 - 2;
        int offsetY = 603;
        batch.draw(tr, offsetX + dimX * (worldController.gridManager.winPos[0]%10), (float) (offsetY - dimY * Math.floor(worldController.gridManager.winPos[0]/10)), 2.5f, 2.5f, 10, 10, 1, 1, 0, false);
        batch.draw(tr, offsetX + dimX * (worldController.gridManager.winPos[1]%10), (float) (offsetY - dimY * Math.floor(worldController.gridManager.winPos[1]/10)), 2.5f, 2.5f, 10, 10, 1, 1, 0, false);
        batch.draw(tr, offsetX + dimX * (worldController.gridManager.winPos[2]%10), (float) (offsetY - dimY * Math.floor(worldController.gridManager.winPos[2]/10)), 2.5f, 2.5f, 10, 10, 1, 1, 0, false);
        batch.draw(tr, offsetX + dimX * (worldController.gridManager.winPos[3]%10), (float) (offsetY - dimY * Math.floor(worldController.gridManager.winPos[3]/10)), 2.5f, 2.5f, 10, 10, 1, 1, 0, false);
    }

    // renders the green lines helping you to choose a column
    private void renderHelpCol(SpriteBatch batch) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(0f, 1f, 0f, 1f);
        pm.fill();

        TextureRegion tr = new TextureRegion(new Texture(pm));
        Vector2 dim = new Vector2(5, 499);
        float offsetX = (cameraGUI.viewportWidth-397)/2;
        float offsetY = 131;
        float dimX = 56;
        batch.draw(tr, offsetX + dimX * worldController.helpCol, offsetY, dim.x/2, dim.y/2, dim.x, dim.y, 1, 1, 0, false);
        batch.draw(tr, offsetX + dimX * (worldController.helpCol+1), offsetY, dim.x/2, dim.y/2, dim.x, dim.y, 1, 1, 0, false);
    }

    // renders who's playing
    private void renderPlayerInfo(SpriteBatch batch) {
        TextureAtlas.AtlasRegion textureRegion;
        if (worldController.gridManager.player == 1)
            if(worldController.isGameFinished) textureRegion = Assets.instance.playerUI.player1won;
            else textureRegion = Assets.instance.playerUI.player1playing;
        else
            if(worldController.isGameFinished) textureRegion = Assets.instance.playerUI.player2won;
            else textureRegion = Assets.instance.playerUI.player2playing;

        float img_ratio = (float) textureRegion.getRegionWidth() / (float) textureRegion.getRegionHeight();
        Vector2 dimension = new Vector2(img_ratio, 1f);
        Vector2 scale = new Vector2(40,40);
        Vector2 origin = new Vector2(dimension.x/2, dimension.y/2);
        float x = dimension.cpy().scl(scale).x/2;
        float y = 30;

        // draw image
        batch.draw(textureRegion.getTexture(), x, y+15, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                0, textureRegion.getRegionX(), textureRegion.getRegionY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight(),
                false, true);

        //Draw Color
        TextureRegion tr;
        if(worldController.gridManager.player==1) tr = Assets.instance.coin.red_coins;
        else tr = Assets.instance.coin.blue_coins;

        Vector2 dim = new Vector2(40, 40);
        batch.draw(tr.getTexture(), 25 + dimension.cpy().scl(scale).x, y, dim.x/2, dim.y/2, dim.x, dim.y, 1, 1, 0,
                tr.getRegionX(), tr.getRegionY(), tr.getRegionWidth(), tr.getRegionHeight(),false, true);
    }

    private void renderWaitingPlayer(SpriteBatch batch){
        TextureAtlas.AtlasRegion textureRegion = Assets.instance.playerUI.waitingPlayer;
        float img_ratio = (float) textureRegion.getRegionWidth() / (float) textureRegion.getRegionHeight();
        Vector2 dimension = new Vector2(img_ratio, 1f);
        Vector2 scale = new Vector2(30,30);
        Vector2 origin = new Vector2(dimension.x/2, dimension.y/2);
        float x = cameraGUI.viewportWidth / 2;
        float y = Constants.VIEWPORT_GUI_HEIGHT / 2;
        batch.draw(textureRegion.getTexture(), x, y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                0, textureRegion.getRegionX(), textureRegion.getRegionY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight(),
                false, true);
    }


    public void resize(int width, int height) {
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT
                / (float)height) * (float)width;
        camera.update();

        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float) height) * (float) width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
    }

}
