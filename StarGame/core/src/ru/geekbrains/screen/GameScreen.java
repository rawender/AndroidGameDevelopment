package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.base.Font;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BonusExplosionPool;
import ru.geekbrains.pool.BonusPool;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.pool.ShieldPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Bonus;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.ButtonExit;
import ru.geekbrains.sprite.EnemyShip;
import ru.geekbrains.sprite.GOMessage;
import ru.geekbrains.sprite.HealthBar;
import ru.geekbrains.sprite.NGButton;
import ru.geekbrains.sprite.PlayerShip;
import ru.geekbrains.sprite.Shield;
import ru.geekbrains.sprite.ShieldBar;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.sprite.StarDust;
import ru.geekbrains.utils.BonusEmitter;
import ru.geekbrains.utils.EnemiesEmitter;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 50;
    private static final int STARDUST_COUNT = 150;
    private static final float FONT_SIZE = 1.3f;
    private static final String SCORE = "Score: ";
    private static final String HP = "HP: ";
    private static final String HIGH_SCORE = "High Score: ";
    private static final String DMG = "DMG: ";
    private static final String LEVEL = "Level: ";

    private enum State {PLAYING, PAUSE, GAME_OVER}
    private enum ShieldState {SHIELD_ON, SHIELD_OFF}

    private Background background;
    private Texture backgroundTexture;
    private TextureAtlas atlas;

    private Star starList[];
    private StarDust starDustList[];
    private PlayerShip playerShip;
    private HealthBar maxHealth;
    private HealthBar currentHealth;
    private ShieldBar shieldBar;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private BonusExplosionPool bonusExplosionPool;
    private BonusPool bonusPool;
    private ShieldPool shieldPool;

    private EnemiesEmitter enemiesEmitter;
    private BonusEmitter bonusEmitter;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosionSound;
    private Sound bonusExplosionSound;

    private int frags;
    private int score;
    public static int highScore;
    static Preferences prefs;

    private State state;
    private State stattBuf;
    private ShieldState shieldState = ShieldState.SHIELD_OFF;


    private GOMessage goMessage;
    private NGButton ngButton;
    private ButtonExit buttonExit;

    private Font font;
    private StringBuilder sbScore;
    private StringBuilder sbHp;
    private StringBuilder sbHighScore;
    private StringBuilder sbDmg;
    private StringBuilder sbLevel;

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("music/game.mp3"));
        music.setLooping(true);
        music.play();
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        bonusExplosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bonus_explosion.wav"));
        backgroundTexture = new Texture("textures/space.jpg");
        background = new Background(new TextureRegion(backgroundTexture));
        atlas = new TextureAtlas("textures/mainAtlas.pack");
        starList = new Star[STAR_COUNT];
        for (int i = 0; i < starList.length; i++) {
            starList[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosionSound);
        bonusExplosionPool = new BonusExplosionPool(atlas, bonusExplosionSound);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, bulletSound);
        bonusPool = new BonusPool(bonusExplosionPool, worldBounds);
        shieldPool = new ShieldPool(atlas, worldBounds);
        playerShip = new PlayerShip(atlas, bulletPool, explosionPool, shieldPool, laserSound);
        maxHealth = new HealthBar(atlas.findRegion("HP"), worldBounds);
        currentHealth = new HealthBar(atlas.findRegion("current_HP"), worldBounds);
        shieldBar = new ShieldBar(atlas.findRegion("current_shield"), worldBounds);
        enemiesEmitter = new EnemiesEmitter(atlas, worldBounds, enemyPool);
        bonusEmitter = new BonusEmitter(atlas, worldBounds, bonusPool);
        goMessage = new GOMessage(atlas);
        ngButton = new NGButton(atlas, this);
        buttonExit = new ButtonExit(atlas);
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
        font.setColor(Color.SKY);
        sbScore = new StringBuilder();
        sbHp = new StringBuilder();
        sbHighScore = new StringBuilder();
        sbDmg = new StringBuilder();
        sbLevel = new StringBuilder();
        prefs = Gdx.app.getPreferences("My Preferences");
        highScore = prefs.getInteger("highscore");
        starDustList = new StarDust[STARDUST_COUNT];
        for (int i = 0; i < starDustList.length; i++) {
            starDustList[i] = new StarDust(atlas, playerShip.getV());
        }
        startNewGame();
    }

    @Override
    public void pause() {
        super.pause();
        stattBuf = state;
        state = State.PAUSE;
    }

    @Override
    public void resume() {
        super.resume();
        state = stattBuf;
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
        if (state == State.PLAYING) {
            playerShip.resize(worldBounds);
        }
        buttonExit.resize(worldBounds);
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
        bonusExplosionPool.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            playerShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            bonusPool.updateActiveSprites(delta);
            for (int i = 0; i < shieldPool.getActiveObjects().size(); i++) {
                Shield shield = shieldPool.getActiveObjects().get(i);
                if (!shield.isDestroyed()) {
                    shield.update(delta, playerShip.pos, shieldBar);
                }
            }
            enemiesEmitter.generate(delta, frags);
            bonusEmitter.generate(delta, enemiesEmitter.getHealthUpMod(), enemiesEmitter.getDamageUpMod(), playerShip.getHp(), playerShip.getDamage());

        }
        if (score > highScore) {
            highScore = score;
        }
    }

    private void checkCollisions() {
        if (state == State.GAME_OVER) {
            return;
        }
        List<EnemyShip> enemyList = enemyPool.getActiveObjects();
        List<Bonus> bonusList = bonusPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        List<Shield> shieldsList = shieldPool.getActiveObjects();
        for (EnemyShip enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + playerShip.getHalfWidth();
            if (shieldState == ShieldState.SHIELD_OFF) {
                if (enemy.pos.dst(playerShip.pos) < minDist) {
                    enemy.damage(enemy.getHp());
                    playerShip.damage(playerShip.getHp());
                    state = State.GAME_OVER;
                    return;
                }
            } else {
                for (Shield shield : shieldsList) {
                    float minShieldDist = enemy.getHalfWidth() + shield.getHalfWidth();
                    if (enemy.pos.dst(shield.pos) < minShieldDist) {
                        enemy.damage(enemy.getHp());
                    }
                }
            }
        }
        for (EnemyShip enemy : enemyList) {
            if (enemy.isDestroyed()) {
                if (shieldState == ShieldState.SHIELD_OFF) {
                    if (enemy.isSelfDestroy()) {
                        playerShip.damage(5 * enemiesEmitter.getDamageUpMod());
                        if (playerShip.isDestroyed()) {
                            state = State.GAME_OVER;
                        }
                    }
                }
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != playerShip || bullet.isDestroyed()) {
                    continue;
                }
                if (!enemy.cheсkState()) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    bullet.destroy();
                    if (enemy.isDestroyed()) {
                        score += enemy.getPoints();
                        frags++;
                    }
                }
            }
        }
        for (Bonus bonus : bonusList) {
            if (bonus.isDestroyed()) {
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != playerShip || bullet.isDestroyed()) {
                    continue;
                }
                if (!bonus.cheсkState()) {
                    continue;
                }
                if (bonus.isBulletCollision(bullet)) {
                    bonus.damage(bullet.getDamage());
                    bullet.destroy();
                    if (bonus.isDestroyed()) {
                        if (bonus.getType().equalsIgnoreCase("heal")) {
                            playerShip.setHp(playerShip.getHP());
                        } else if (bonus.getType().equalsIgnoreCase("damage")) {
                            playerShip.setDamage(playerShip.getDamage() + 1);
                        } else {
                            if (shieldState == ShieldState.SHIELD_OFF) {
                                playerShip.shieldUp();
                                shieldState = ShieldState.SHIELD_ON;
                            }
                        }
                    }
                }
            }

        }
        for (Shield shield : shieldsList) {
            if (shield.isDestroyed()) {
                shieldState = ShieldState.SHIELD_OFF;
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() == playerShip || bullet.isDestroyed()) {
                    continue;
                }
                if (shield.isBulletCollision(bullet)) {
                    bullet.destroy();
                }
            }

        }
        for (Bullet bullet : bulletList) {
            if (bullet.getOwner() == playerShip || bullet.isDestroyed()) {
                continue;
            }
            if (shieldState == ShieldState.SHIELD_OFF) {
                if (playerShip.isBulletCollision(bullet)) {
                    playerShip.damage(bullet.getDamage());
                    bullet.destroy();
                    if (playerShip.isDestroyed()) {
                        state = State.GAME_OVER;
                    }
                }
            }
        }
    }

    private void deleteAllDestroyed() {
        bulletPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
        bonusExplosionPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
        bonusPool.freeAllDestroyedActiveSprites();
        shieldPool.freeAllDestroyedActiveSprites();
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
        bonusExplosionPool.drawActiveSprites(batch);
        if (state == State.PLAYING) {
            playerShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
            bonusPool.drawActiveSprites(batch);
            shieldPool.drawActiveSprites(batch);
            if (shieldState == ShieldState.SHIELD_ON) {
                shieldBar.draw(batch);
            }
        } else if (state == State.GAME_OVER) {
            goMessage.draw(batch);
            ngButton.draw(batch);
            buttonExit.draw(batch);
        }
        printInfo();
        batch.end();
    }

    public void printInfo() {
        sbScore.setLength(0);
        //sbHp.setLength(0);
        sbHighScore.setLength(0);
        sbDmg.setLength(0);
        sbLevel.setLength(0);
        maxHealth.setSize(0.86f, 7f, playerShip.getHP());
        currentHealth.setSize(0.86f, 7f, playerShip.getHp());
        maxHealth.draw(batch);
        currentHealth.draw(batch);
        font.draw(batch, sbScore.append(SCORE).append(score), worldBounds.getLeft() + 1f, worldBounds.getTop() - 1.1f, Align.left);
        //font.draw(batch, sbHp.append(HP).append(playerShip.getHp()), worldBounds.pos.x, worldBounds.getTop() - 1f, Align.center);
        font.draw(batch, sbHighScore.append(HIGH_SCORE).append(highScore), worldBounds.getRight() - 1f, worldBounds.getTop() - 1.1f, Align.right);
        font.draw(batch, sbDmg.append(DMG).append(playerShip.getDamage()), worldBounds.getLeft() + 1f, worldBounds.getTop() - 3.6f, Align.left);
        font.draw(batch, sbLevel.append(LEVEL).append(enemiesEmitter.getLevel()), worldBounds.getRight() - 1f, worldBounds.getTop() - 3.6f, Align.right);
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        atlas.dispose();
        music.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        explosionSound.dispose();
        bonusExplosionSound.dispose();
        explosionPool.dispose();
        bonusExplosionPool.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        bonusPool.dispose();
        shieldPool.dispose();
        font.dispose();
        prefs.putInteger("highscore", highScore);
        prefs.flush();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (state == State.PLAYING) {
            playerShip.touchDown(touch, pointer);
        } else if (state == State.GAME_OVER) {
            ngButton.touchDown(touch, pointer);
            buttonExit.touchDown(touch, pointer);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (state == State.PLAYING) {
            playerShip.touchUp(touch, pointer);
        } else if (state == State.GAME_OVER) {
            buttonExit.touchUp(touch, pointer);
            ngButton.touchUp(touch, pointer);
        }
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        if (state == State.PLAYING) {
            //playerShip.touchDragged(touch, pointer);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            playerShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            playerShip.keyUp(keycode);
        }
        return false;
    }

    public void startNewGame() {
        state = State.PLAYING;
        frags = 0;
        score = 0;
        playerShip.startNewGame(worldBounds);
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        bonusPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        bonusExplosionPool.freeAllActiveObjects();
    }
}
