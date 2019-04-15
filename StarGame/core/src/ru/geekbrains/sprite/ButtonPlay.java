package ru.geekbrains.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.screen.GameScreen;

public class ButtonPlay extends ScaledButton {

    private Game game;

    public ButtonPlay(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("btnStart"));
        this.game = game;
        setHeightProportion(8f);
        setTop(10f);
    }

    @Override
    protected void action() {
        game.setScreen(new GameScreen());
    }
}
