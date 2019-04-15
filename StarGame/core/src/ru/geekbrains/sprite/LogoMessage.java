package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.Sprite;

public class LogoMessage extends Sprite {

    public LogoMessage(TextureAtlas atlas) {
        super(atlas.findRegion("message_space_shooter"));
        setHeightProportion(3f);
        setBottom(15f);
    }
}
