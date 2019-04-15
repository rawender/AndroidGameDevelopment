package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.screen.GameScreen;

public class NGButton extends ScaledButton {

    private GameScreen gameScreen;

    public NGButton(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("btnNewGame"));
        setHeightProportion(8f);
        setTop(10f);
        this.gameScreen = gameScreen;
    }

    @Override
    protected void action() {
        gameScreen.startNewGame();
    }
}
