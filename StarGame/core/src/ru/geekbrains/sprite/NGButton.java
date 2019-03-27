package ru.geekbrains.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.GameScreen;

public class NGButton extends ScaledButton {

    private Game game;

    public NGButton(TextureAtlas atlas, Game game) {

        super(atlas.findRegion("button_new_game"));
        this.game = game;
        setHeightProportion(4f);
    }

    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getBottom() + 40f);
    }

    @Override
    protected void action() {
        game.setScreen(new GameScreen(game));
    }
}
