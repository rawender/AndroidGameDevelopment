package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class StarDust extends Sprite {

    private float starHeightMin;
    private float starHeightMax;
    private float starHeight;
    private Vector2 v;
    private Rect worldBounds;

    public StarDust(TextureAtlas atlas) {
        super(atlas.findRegion("star"));
        float vX = Rnd.nextFloat(-0.05f, 0.05f);
        float vY = Rnd.nextFloat(-50f, -10f);
        v = new Vector2(vX, vY);
        starHeightMin = 0.3f;
        starHeightMax = 0.4f;
        starHeight = Rnd.nextFloat(starHeightMin, starHeightMax);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        checkAndHandleBounds();
        if (starHeight >= starHeightMax) {
            starHeight = starHeightMin;
        } else {
            starHeight += 0.001f;
        }
        setHeightProportion(starHeight);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float posX = Rnd.nextFloat(worldBounds.getLeft(),worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(posX, posY);
    }

    private void checkAndHandleBounds() {
        if (getRight() < worldBounds.getLeft()) {
            setLeft(worldBounds.getRight());
        }
        if (getLeft() > worldBounds.getRight()) {
            setRight(worldBounds.getLeft());
        }
        if (getTop() < worldBounds.getBottom()) {
            setBottom(worldBounds.getTop());
        }
        if (getBottom() > worldBounds.getTop()) {
            setTop(worldBounds.getBottom());
        }
    }
}
