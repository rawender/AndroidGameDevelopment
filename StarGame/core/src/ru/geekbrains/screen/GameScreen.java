package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.BattleShip;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.sprite.StarDust;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 70;
    private static final int STARDUST_COUNT = 230;

    private Texture backgroundTexture;
    private Texture battleShipTexture;
    private Background background;
    private BattleShip battleShip;
    private TextureAtlas atlas;
    private Star starList[];
    private StarDust starDustList[];
    private Music music = Gdx.audio.newMusic(Gdx.files.internal("music/game.mp3"));

    @Override
    public void show() {
        super.show();
        backgroundTexture = new Texture("textures/space.jpg");
        background = new Background(new TextureRegion(backgroundTexture));
        battleShipTexture = new Texture("textures/battleship.png");
        battleShip = new BattleShip(new TextureRegion(battleShipTexture));
        atlas = new TextureAtlas("textures/menuAtlas.tpack");
        starList = new Star[STAR_COUNT];
        for (int i = 0; i < starList.length; i++) {
            starList[i] = new Star(atlas);
        }
        starDustList = new StarDust[STARDUST_COUNT];
        for (int i = 0; i < starDustList.length; i++) {
            starDustList[i] = new StarDust(atlas);
        }
        music.setLooping(true);
        music.play();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : starList) {
            star.resize(worldBounds);
        }
        for (StarDust starDust : starDustList) {
            starDust.resize(worldBounds);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    private void update(float delta) {
        for (Star star : starList) {
            star.update(delta);
        }
        for (StarDust starDust : starDustList) {
            starDust.update(delta);
        }
    }

    private void  draw() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star : starList) {
            star.draw(batch);
        }
        for (StarDust starDust : starDustList) {
            starDust.draw(batch);
        }
        battleShip.draw(batch);
        batch.end();
        battleShip.update();
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        atlas.dispose();
        battleShipTexture.dispose();
        music.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        battleShip.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        battleShip.touchDown(touch, pointer);
        return false;
    }
}
