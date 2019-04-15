package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class ShieldBar extends Sprite {

    private Rect worldBounds;

    public ShieldBar(TextureRegion region, Rect worldBounds) {
        super(region);
        this.worldBounds = worldBounds;
    }
    public void setSize(float halfHeight, float halfWidth, int count) {
        this.halfHeight = halfHeight;
        this.halfWidth = halfWidth * ((10f - (float) count)/10f);
        setTop(worldBounds.getTop() - 3f);
        //setLeft(-halfWidth);
    }
}
