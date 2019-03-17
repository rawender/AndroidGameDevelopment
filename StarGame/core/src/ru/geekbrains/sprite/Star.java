package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class Star extends Sprite {

    private float starHeightMin;
    private float starHeightMax;
    private float starHeight;
    private float shine;

    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("star"));
        starHeightMin = 0.5f;
        starHeightMax = 0.6f;
        starHeight = Rnd.nextFloat(starHeightMin, starHeightMax);
        shine = Rnd.nextFloat(0.001f, 0.002f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (starHeight >= starHeightMax) {
            starHeight = starHeightMin;
        } else {
            starHeight += shine;
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
