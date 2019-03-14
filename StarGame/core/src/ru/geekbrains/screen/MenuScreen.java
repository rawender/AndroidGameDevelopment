package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.BattleShip;

public class MenuScreen extends BaseScreen {

    private Texture img1;
    private Texture img2;
    private Background background;
    private BattleShip battleShip;


    @Override
    public void show() {
        super.show();
        img1 = new Texture("space.jpg");
        img2 = new Texture("bgbattleship.png");
        background = new Background(new TextureRegion(img1));
        battleShip = new BattleShip(new TextureRegion(img2));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        battleShip.draw(batch);
        batch.end();
        battleShip.update();
    }

    @Override
    public void dispose() {
        img1.dispose();
        img2.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        battleShip.touchDown(touch, pointer);
        return false;
    }
}
