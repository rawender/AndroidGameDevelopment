package ru.geekbrains.pool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.SpritesPool;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Shield;

public class ShieldPool extends SpritesPool<Shield> {

    private TextureAtlas atlas;
    private Rect worldBounds;

    public ShieldPool(TextureAtlas atlas, Rect worldBounds) {
        this.atlas = atlas;
        this.worldBounds = worldBounds;
    }

    @Override
    protected Shield newObject() {
        return new Shield(atlas, worldBounds);
    }
}
