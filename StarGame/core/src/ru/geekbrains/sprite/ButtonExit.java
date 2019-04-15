package ru.geekbrains.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;

public class ButtonExit extends ScaledButton {
    public ButtonExit(TextureAtlas atlas) {
        super(atlas.findRegion("btnExit"));
        setHeightProportion(8f);
        setBottom(-10f);
    }

    @Override
    protected void action() {
        Gdx.app.exit();
    }
}
