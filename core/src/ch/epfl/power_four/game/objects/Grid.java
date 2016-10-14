package ch.epfl.power_four.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import ch.epfl.power_four.game.util.Assets;
import ch.epfl.power_four.game.util.BodyEditorLoader;
import ch.epfl.power_four.game.util.Constants;

public class Grid extends AbstractGameObject {

    public Grid(){
        super(Assets.instance.grid.grid);
        init();
    }

    private void init(){
        setDimension(4,5);
    }

    @Override
    public void initPhysics(World world){
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal(Constants.BOUNDS_COLLISION));

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x + dimension.x / 2, position.y + dimension.y / 2);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0.05f;
        fixtureDef.density = 15f;

        body = world.createBody(bodyDef);

        loader.attachFixture(body, "grid", fixtureDef, dimension.x, dimension.y/1.58f);
    }
}
