package ru.geekbrains.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprite.EnemyShip;

public class EnemiesEmitter {

    private static final float ENEMY_SMALL_HEIGHT = 8f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.75f;
    private static final float ENEMY_SMALL_BULLET_VY = -30f;
    private static final int ENEMY_SMALL_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 1f;
    private static final int ENEMY_SMALL_HP = 1;

    private static final float ENEMY_AVERAGE_HEIGHT = 10f;
    private static final float ENEMY_AVERAGE_BULLET_HEIGHT = 1f;
    private static final float ENEMY_AVERAGE_BULLET_VY = -25f;
    private static final int ENEMY_AVERAGE_DAMAGE = 3;
    private static final float ENEMY_AVERAGE_RELOAD_INTERVAL = 2f;
    private static final int ENEMY_AVERAGE_HP = 3;

    private static final float ENEMY_BIG_HEIGHT = 12f;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 1.25f;
    private static final float ENEMY_BIG_BULLET_VY = -20f;
    private static final int ENEMY_BIG_DAMAGE = 5;
    private static final float ENEMY_BIG_RELOAD_INTERVAL = 3f;
    private static final int ENEMY_BIG_HP = 5;

    private Rect worldBounds;

    private float generateInterval = 3f;
    private float generateTimer;

    private TextureRegion[] enemySmallRegion;
    private Vector2 enemySmallV = new Vector2(0f, -20f);
    private TextureRegion[] enemyAverageRegion;
    private Vector2 enemyAverageV = new Vector2(0f, -15f);
    private TextureRegion[] enemyBigRegion;
    private Vector2 enemyBigV = new Vector2(0f, -10f);

    private TextureRegion bulletRegion;

    private EnemyPool enemyPool;

    private float x;

    public EnemiesEmitter(TextureAtlas atlas, Rect worldBounds, EnemyPool enemyPool) {
        this.worldBounds = worldBounds;
        this.enemyPool = enemyPool;
        TextureRegion textureRegion0 = atlas.findRegion("enemy0");
        this.enemySmallRegion = Regions.split(textureRegion0, 1, 2, 2);
        TextureRegion textureRegion1 = atlas.findRegion("enemy1");
        this.enemyAverageRegion = Regions.split(textureRegion1, 1, 2, 2);
        TextureRegion textureRegion2 = atlas.findRegion("enemy2");
        this.enemyBigRegion = Regions.split(textureRegion2, 1, 2, 2);
        this.bulletRegion = atlas.findRegion("bulletEnemy");
    }

    public void generate(float delta) {
        generateTimer += delta;
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            x = Rnd.nextFloat(0f, 10f);
            if (x >= 0f && x <= 5f) {
                EnemyShip enemyShip = enemyPool.obtain();
                enemyShip.set(
                        enemySmallRegion,
                        enemySmallV,
                        bulletRegion,
                        ENEMY_SMALL_BULLET_HEIGHT,
                        ENEMY_SMALL_BULLET_VY,
                        ENEMY_SMALL_DAMAGE,
                        ENEMY_SMALL_RELOAD_INTERVAL,
                        ENEMY_SMALL_HEIGHT,
                        ENEMY_SMALL_HP
                );
                enemyShip.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemyShip.getHalfWidth(), worldBounds.getRight() - enemyShip.getHalfWidth());
                enemyShip.setBottom(worldBounds.getTop());
            } else if (x >= 6f && x <= 8f) {
                EnemyShip enemyShip = enemyPool.obtain();
                enemyShip.set(
                        enemyAverageRegion,
                        enemyAverageV,
                        bulletRegion,
                        ENEMY_AVERAGE_BULLET_HEIGHT,
                        ENEMY_AVERAGE_BULLET_VY,
                        ENEMY_AVERAGE_DAMAGE,
                        ENEMY_AVERAGE_RELOAD_INTERVAL,
                        ENEMY_AVERAGE_HEIGHT,
                        ENEMY_AVERAGE_HP
                );
                enemyShip.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemyShip.getHalfWidth(), worldBounds.getRight() - enemyShip.getHalfWidth());
                enemyShip.setBottom(worldBounds.getTop());
            } else if (x >= 9f && x <= 10f) {
                EnemyShip enemyShip = enemyPool.obtain();
                enemyShip.set(
                        enemyBigRegion,
                        enemyBigV,
                        bulletRegion,
                        ENEMY_BIG_BULLET_HEIGHT,
                        ENEMY_BIG_BULLET_VY,
                        ENEMY_BIG_DAMAGE,
                        ENEMY_BIG_RELOAD_INTERVAL,
                        ENEMY_BIG_HEIGHT,
                        ENEMY_BIG_HP
                );
                enemyShip.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemyShip.getHalfWidth(), worldBounds.getRight() - enemyShip.getHalfWidth());
                enemyShip.setBottom(worldBounds.getTop());
            }
        }
    }
}
