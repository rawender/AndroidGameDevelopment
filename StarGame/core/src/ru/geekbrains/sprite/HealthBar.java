package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class HealthBar extends Sprite {

    private Rect worldBounds;

    public HealthBar(TextureRegion region, Rect worldBounds) {
        super(region);
        this.worldBounds = worldBounds;
    }

    public void setSize(float halfHeight, float halfWidth, int health) {
        if (health < 0) {
            health = 0;
        }
        this.halfHeight = halfHeight;
        this.halfWidth = halfWidth * ((float) health/100f);
        setTop(worldBounds.getTop() - 0.9f);
        setLeft(-halfWidth);
    }
}
