package ch.epfl.power_four.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class AbstractGameObject {
    public Body body;

    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;
    public Rectangle bounds;

    public TextureRegion textureRegion;

    public AbstractGameObject(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        float img_ratio = (float) textureRegion.getRegionWidth() / (float) textureRegion.getRegionHeight();
        dimension = new Vector2(img_ratio, 1f);
        init();
    }

    private void init() {
        position = new Vector2();
        origin = new Vector2(dimension.x / 2, dimension.y / 2);
        scale = new Vector2(1, 1);
        rotation = 0;
        bounds = new Rectangle(0, 0, dimension.x, dimension.y);
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        float img_ratio = (float) textureRegion.getRegionWidth() / (float) textureRegion.getRegionHeight();
        dimension.set(img_ratio, 1f);
        // Center image on game object
        origin.set(dimension.x / 2, dimension.y / 2);
        // Bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
    }

    public void resize(float factor) {
        dimension.set(dimension.x * factor, dimension.y * factor);
        origin.set(dimension.x / 2, dimension.y / 2);
        bounds.set(0, 0, dimension.x, dimension.y);
    }

    public void setDimension(float x, float y) {
        dimension.set(x, y);
        origin.set(dimension.x / 2, dimension.y / 2);
        bounds.set(0, 0, dimension.x, dimension.y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureRegion.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                rotation, textureRegion.getRegionX(), textureRegion.getRegionY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight(),
                false, false);
    }

    public void update(float deltaTime) {
        Vector2 t = body.getPosition();
        position.set(t.x - dimension.x / 2, t.y - dimension.y / 2);
        rotation = body.getAngle() * MathUtils.radiansToDegrees;
    }

    public void initPhysics(World world){}
}
