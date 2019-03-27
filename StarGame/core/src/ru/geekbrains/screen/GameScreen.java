package ru.geekbrains.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.EnemyShip;
import ru.geekbrains.sprite.GOMessage;
import ru.geekbrains.sprite.NGButton;
import ru.geekbrains.sprite.PlayerShip;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.sprite.StarDust;
import ru.geekbrains.utils.EnemiesEmitter;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 70;
    private static final int STARDUST_COUNT = 230;

    private Game game;

    private Background background;
    private Texture backgroundTexture;
    private TextureAtlas atlas;

    private Star starList[];
    private StarDust starDustList[];
    private PlayerShip playerShip;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;

    private EnemiesEmitter enemiesEmitter;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosionSound;

    private GOMessage goMessage;
    private NGButton ngButton;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("music/game.mp3"));
        music.setLooping(true);
        music.play();
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
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
        explosionPool = new ExplosionPool(atlas, explosionSound);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, bulletSound);
        playerShip = new PlayerShip(atlas, bulletPool, explosionPool, laserSound);
        enemiesEmitter = new EnemiesEmitter(atlas, worldBounds, enemyPool);
        goMessage = new GOMessage(atlas);
        ngButton = new NGButton(atlas, game);
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
        if (!playerShip.isDestroyed()) {
            playerShip.resize(worldBounds);
        }
        goMessage.resize(worldBounds);
        ngButton.resize(worldBounds);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
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
        explosionPool.updateActiveSprites(delta);
        if (!playerShip.isDestroyed()) {
            playerShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemiesEmitter.generate(delta);
        }
    }

    private void checkCollisions() {
        if (playerShip.isDestroyed()) {
            return;
        }
        List<EnemyShip> enemyList = enemyPool.getActiveObjects();
        for (EnemyShip enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + playerShip.getHalfWidth();
            if (enemy.pos.dst(playerShip.pos) < minDist) {
                enemy.damage(enemy.getHp());
                playerShip.damage(playerShip.getHp());
                return;
            }
        }

        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (EnemyShip enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != playerShip || bullet.isDestroyed()) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    bullet.destroy();
                }
            }
        }

        for (Bullet bullet : bulletList) {
            if (bullet.getOwner() == playerShip || bullet.isDestroyed()) {
                continue;
            }
            if (playerShip.isBulletCollision(bullet)) {
                playerShip.damage(bullet.getDamage());
                bullet.destroy();
            }
        }
    }

    private void deleteAllDestroyed() {
        bulletPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
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
        explosionPool.drawActiveSprites(batch);
        if (!playerShip.isDestroyed()) {
            playerShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        } else {
            goMessage.draw(batch);
            ngButton.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        atlas.dispose();
        music.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        explosionSound.dispose();
        explosionPool.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (!playerShip.isDestroyed()) {
            playerShip.touchDown(touch, pointer);
        } else {
            ngButton.touchDown(touch, pointer);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (!playerShip.isDestroyed()) {
            playerShip.touchUp(touch, pointer);
        } else {
            ngButton.touchUp(touch, pointer);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!playerShip.isDestroyed()) {
            playerShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!playerShip.isDestroyed()) {
            playerShip.keyUp(keycode);
        }
        return false;
    }
}
