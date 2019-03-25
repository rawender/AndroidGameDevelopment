package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.PlayerShip;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.sprite.StarDust;
import ru.geekbrains.utils.EnemiesEmitter;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 70;
    private static final int STARDUST_COUNT = 230;

    private Texture backgroundTexture;
    private Background background;
    private TextureAtlas atlas;

    private Star starList[];
    private StarDust starDustList[];
    private PlayerShip playerShip;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;

    private EnemiesEmitter enemiesEmitter;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("music/game.mp3"));
        music.setLooping(true);
        music.play();
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        backgroundTexture = new Texture("textures/space.jpg");
        background = new Background(new TextureRegion(backgroundTexture));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        starList = new Star[STAR_COUNT];
        for (int i = 0; i < starList.length; i++) {
            starList[i] = new Star(atlas);
        }
        starDustList = new StarDust[STARDUST_COUNT];
        for (int i = 0; i < starDustList.length; i++) {
            starDustList[i] = new StarDust(atlas);
        }
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool(bulletPool, worldBounds, bulletSound);
        playerShip = new PlayerShip(atlas, bulletPool, laserSound);
        enemiesEmitter = new EnemiesEmitter(atlas, worldBounds, enemyPool);
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
        playerShip.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        deleteAllDestroyed();
        draw();
    }

    private void update(float delta) {
        for (Star star : starList) {
            star.update(delta);
        }
        for (StarDust starDust : starDustList) {
            starDust.update(delta);
        }
        playerShip.update(delta);
        bulletPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);
        enemiesEmitter.generate(delta);
    }

    private void deleteAllDestroyed() {
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
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
        playerShip.draw(batch);
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        atlas.dispose();
        music.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        playerShip.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        playerShip.touchUp(touch, pointer);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        playerShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        playerShip.keyUp(keycode);
        return false;
    }
}
