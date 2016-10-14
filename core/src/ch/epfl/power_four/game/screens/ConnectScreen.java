package ch.epfl.power_four.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

import ch.epfl.power_four.PowerFourMain;
import ch.epfl.power_four.game.WorldController;
import ch.epfl.power_four.game.network.BroadcasterMediator;
import ch.epfl.power_four.game.network.Player;
import ch.epfl.power_four.game.network.Server;
import ch.epfl.power_four.game.util.Constants;

public class ConnectScreen extends AbstractGameScreen{
    private Skin skinConnect;
    private Skin uiSkin;
    private Stage stage;


    private Label ipAddress;
    private Label port;

    private Label labelIP;
    private Label labelPort;
    private TextField dest_ipAddress;
    private TextField dest_port;

    private Button btnConnect;

    private WorldController worldController;

    public ConnectScreen(Game game) {
        super(game);
        worldController = new WorldController();
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.5f, 0.3f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
    }

    @Override
    public void hide() {
        stage.dispose();
        skinConnect.dispose();
        uiSkin.dispose();
    }

    @Override
    public void pause() {}

    private void rebuildStage() {
        skinConnect = new Skin(
                Gdx.files.internal(Constants.SKIN_CONNECT_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_CONNECT_UI));
        uiSkin = new Skin(Gdx.files.internal(Constants.SKIN_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));

        // build all layers
        Table layerControls = buildControlsLayer();
        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerControls);
    }

    private Table buildControlsLayer() {
        Table layer = new Table();
        layer.center();


        Table tLayerInfo = new Table();
        ipAddress = new Label(getIPAddress()+":", uiSkin);
        tLayerInfo.add(ipAddress);
        port = new Label(Integer.toString(PowerFourMain.PORT), uiSkin);
        tLayerInfo.add(port).pad(10);
        layer.add(tLayerInfo);
        layer.row();

        Table tLayerIP = new Table();
        labelIP = new Label("Player's IP: ", uiSkin);
        tLayerIP.add(labelIP);
        dest_ipAddress = new TextField("", uiSkin);
        tLayerIP.add(dest_ipAddress);
        layer.add(tLayerIP).pad(10);
        layer.row();

        Table tLayerPort = new Table();
        labelPort = new Label("Player's port: ", uiSkin);
        tLayerPort.add(labelPort);
        dest_port = new TextField(PowerFourMain.PORT+"", uiSkin);
        tLayerPort.add(dest_port);
        layer.add(tLayerPort).pad(10);
        layer.row();

        btnConnect = new Button(skinConnect, "btnConnect");
        layer.add(btnConnect).pad(20f);
        btnConnect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onBtnConnectClicked();
            }
        });

        return layer;
    }

    private void onBtnConnectClicked() {

        Server s = new Server(worldController);
        s.startServer(PowerFourMain.PORT);

        if (dest_ipAddress.getText().equals("")){
            game.setScreen(new GameScreen(game, worldController));
            worldController.activate();
        } else{
            try {
                worldController.otherPlayer = new Player(2, InetAddress.getByName(dest_ipAddress.getText()), Integer.parseInt(dest_port.getText()));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            BroadcasterMediator bm = new BroadcasterMediator(worldController);
            bm.connect(dest_ipAddress.getText(),dest_port.getText(),getIPAddress(), PowerFourMain.PORT);
            game.setScreen(new GameScreen(game, worldController));
            worldController.activate();
        }
    }
    
    public static String getIPAddress() {
        try {
            ArrayList<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                ArrayList<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':')<0;
                        if (isIPv4) return sAddr;
                    }
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return "";
    }
}
