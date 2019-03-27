package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class GOMessage extends Sprite {

    public GOMessage(TextureAtlas atlas) {
        super(atlas.findRegion("message_game_over"));
        setHeightProportion(4f);
    }

    @Override
    public void resize(Rect worldBounds) {
        setTop(worldBounds.getTop() - 40f);
    }
}
