package ch.epfl.power_four.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

    private AssetManager assetManager;
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();

    public AssetCoin coin;
    public AssetGrid grid;
    public AssetPlayerUI playerUI;

    private Assets() {}


    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);

        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "asset: " + a);
        }


        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
        // enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        coin = new AssetCoin(atlas);
        grid = new AssetGrid(atlas);
        playerUI = new AssetPlayerUI(atlas);
    }

    public class AssetCoin {
        public final TextureAtlas.AtlasRegion blue_coins;
        public final TextureAtlas.AtlasRegion red_coins;

        public AssetCoin (TextureAtlas atlas) {
            blue_coins = atlas.findRegion("blue_coins");
            red_coins = atlas.findRegion("red_coins");
        }
    }

    public class AssetGrid{
        public final TextureAtlas.AtlasRegion grid;

        public AssetGrid (TextureAtlas atlas) {
            grid = atlas.findRegion("grid");
        }
    }

    public class AssetPlayerUI{
        public final TextureAtlas.AtlasRegion player1playing;
        public final TextureAtlas.AtlasRegion player2playing;
        public final TextureAtlas.AtlasRegion player1won;
        public final TextureAtlas.AtlasRegion player2won;
        public final TextureAtlas.AtlasRegion tie;
        public final TextureAtlas.AtlasRegion waitingPlayer;

        public AssetPlayerUI (TextureAtlas atlas) {
            player1playing = atlas.findRegion("player1playing");
            player1won = atlas.findRegion("player1won");
            player2playing = atlas.findRegion("player2playing");
            player2won = atlas.findRegion("player2won");
            waitingPlayer = atlas.findRegion("waiting_other_player");
            tie = atlas.findRegion("tie");
        }
    }


    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
