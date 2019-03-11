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
    private Vector2 path;
    private int h;
    private int w;
    private float pathLength;
    private int onPath;

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        background = new Texture("space.jpg");
        img = new Texture("bgbattleship.png");
        h = img.getHeight() / 2;
        w = img.getWidth() / 2;
        touch = new Vector2();
        pos = new Vector2();
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
        path = touch.cpy().sub(pos);
        pathLength = path.len();
        path.nor();
        if (onPath < pathLength) {
            pos.add(path);
            onPath++;
        } else if (onPath >= pathLength) {
            pos.setZero();
            onPath = 0;
            pos.set(touch.x, touch.y);
        }
        /*if (pos.x == 0 && pos.y == 0) {
            pos.set(touch.x, touch.y);
        } else {
            pos.set(touch.x - w, touch.y - h);
        }*/
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
        return super.touchDown(screenX, screenY, pointer, button);
    }
}
