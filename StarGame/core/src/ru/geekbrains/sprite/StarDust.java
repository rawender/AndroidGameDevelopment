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

    private Vector2 trackingV;
    private Vector2 sumV = new Vector2();

    public StarDust(TextureAtlas atlas, Vector2 trackingV) {
        super(atlas.findRegion("star"));
        this.trackingV = trackingV;
        float vX = Rnd.nextFloat(-0.05f, 0.05f);
        float vY = Rnd.nextFloat(-50f, -10f);
        v = new Vector2(vX, vY);
        starHeightMin = 0.25f;
        starHeightMax = 0.35f;
        starHeight = Rnd.nextFloat(starHeightMin, starHeightMax);
    }

    @Override
    public void update(float delta) {
        sumV.setZero().mulAdd(trackingV, 0.2f).rotate(180).add(v);
        this.pos.mulAdd(sumV, delta);
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
