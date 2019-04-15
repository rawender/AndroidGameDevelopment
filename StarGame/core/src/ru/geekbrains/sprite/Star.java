package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class Star extends Sprite {

    private float starHeightMin;
    private float starHeightMax;
    private float starHeight;
    private float animateInterval;
    private float animateTimer;

    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("pulsating_star"), 1, 14, 14);
        starHeightMin = 1f;
        starHeightMax = 2f;
        starHeight = Rnd.nextFloat(starHeightMin, starHeightMax);
        animateInterval = Rnd.nextFloat(0.15f, 0.25f);
    }

    @Override
    public void update(float delta) {
        animateTimer += delta;
        if (animateTimer >= animateInterval) {
            animateTimer = 0f;
            if (++frame == regions.length) {
                frame = 0;
            }
        }
        setHeightProportion(starHeight);
    }

    @Override
    public void resize(Rect worldBounds) {
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(posX, posY);
    }
}
