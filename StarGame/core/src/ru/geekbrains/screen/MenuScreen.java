package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;

public class MenuScreen extends BaseScreen {
    private SpriteBatch batch;
    private Texture background;
    private Texture img;
    private Vector2 touch;
    private Vector2 pos;
    private Vector2 newPos;
    private Vector2 path;
    private int h;
    private int w;
    private float pathLength;

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        background = new Texture("space.jpg");
        img = new Texture("bgbattleship.png");
        h = img.getHeight() / 2;
        w = img.getWidth() / 2;
        touch = new Vector2();
        pos = new Vector2(Gdx.graphics.getWidth()/2 - w, touch.y);
        newPos = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0);
        batch.draw(img, pos.x, pos.y);
        batch.end();

        if (pos.x == Gdx.graphics.getWidth()/2 - w && pos.y == 0) {
            newPos.set(Gdx.graphics.getWidth()/2 - w, touch.y);
        } else {
            newPos.set(touch.x - w, touch.y - h);
        }

        path = newPos.cpy().sub(pos);
        pathLength = path.len();
        Vector2 speed = path.cpy().nor();
        pos.add(speed);
        pathLength--;

        if (pathLength <= 0) {
            path.setZero();
            pos.set(newPos);
        }


    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        img.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        //pos.set(touch.x - w, touch.y - h);
        return super.touchDown(screenX, screenY, pointer, button);
    }
}
