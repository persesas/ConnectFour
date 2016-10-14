package ch.epfl.power_four.game.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import ch.epfl.power_four.game.util.Assets;

public class Coin extends AbstractGameObject {
    public int player;

    public Coin(int player){
        super(Assets.instance.coin.red_coins);
        this.player = player;
        init();
    }

    private void init(){
        if(player==2) setTextureRegion(Assets.instance.coin.blue_coins);
        setDimension(0.493f, 0.493f);
    }

    @Override
    public void initPhysics(World world) {

        BodyDef coinBodyDef = new BodyDef();
        coinBodyDef.type = BodyDef.BodyType.DynamicBody;
        coinBodyDef.position.set(position.x + dimension.x / 2, position.y + dimension.y / 2);

        this.body = world.createBody(coinBodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(dimension.x / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        body.createFixture(fixtureDef);
        circleShape.dispose();
    }
}
