package ru.geekbrains.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprite.EnemyShip;

public class EnemiesEmitter {

    private static final float ENEMY_SMALL_HEIGHT = 10f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 3f;
    private static final float ENEMY_SMALL_BULLET_VY = -25f;
    private static final int ENEMY_SMALL_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 1f;
    private static final int ENEMY_SMALL_HP = 1;
    private static final int ENEMY_SMALL_POINTS = 5;

    private static final float ENEMY_AVERAGE_HEIGHT = 13f;
    private static final float ENEMY_AVERAGE_BULLET_HEIGHT = 5f;
    private static final float ENEMY_AVERAGE_BULLET_VY = -22.5f;
    private static final int ENEMY_AVERAGE_DAMAGE = 5;
    private static final float ENEMY_AVERAGE_RELOAD_INTERVAL = 2f;
    private static final int ENEMY_AVERAGE_HP = 5;
    private static final int ENEMY_AVERAGE_POINTS = 25;

    private static final float ENEMY_BIG_HEIGHT = 20f;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 7f;
    private static final float ENEMY_BIG_BULLET_VY = -20f;
    private static final int ENEMY_BIG_DAMAGE = 10;
    private static final float ENEMY_BIG_RELOAD_INTERVAL = 3f;
    private static final int ENEMY_BIG_HP = 10;
    private static final int ENEMY_BIG_POINTS = 50;

    private Rect worldBounds;

    private float generateInterval = 3f;
    private float generateTimer;

    private TextureRegion[] enemySmallRegion;
    private Vector2 enemySmallV = new Vector2(0f, -10f);
    private TextureRegion[] enemyAverageRegion;
    private Vector2 enemyAverageV = new Vector2(0f, -5.5f);
    private TextureRegion[] enemyBigRegion;
    private Vector2 enemyBigV = new Vector2(0f, -1f);

    private TextureRegion[] bulletRegions0;
    //private TextureRegion[] bulletRegions1;
    //private TextureRegion[] bulletRegions2;

    private EnemyPool enemyPool;

    private int level;
    private int damageUpMod;
    private int healthUpMod;
    private int bulletUpMod;

    public EnemiesEmitter(TextureAtlas atlas, Rect worldBounds, EnemyPool enemyPool) {
        this.worldBounds = worldBounds;
        this.enemyPool = enemyPool;
        TextureRegion textureRegion0 = atlas.findRegion("enemy0");
        this.enemySmallRegion = Regions.split(textureRegion0, 1, 2, 2);
        TextureRegion textureRegion1 = atlas.findRegion("enemy1");
        this.enemyAverageRegion = Regions.split(textureRegion1, 1, 2, 2);
        TextureRegion textureRegion2 = atlas.findRegion("enemy2");
        this.enemyBigRegion = Regions.split(textureRegion2, 1, 2, 2);
        this.bulletRegions0 = Regions.split(atlas.findRegion("bulletEnemy0"), 1, 16, 16);
        //this.bulletRegions1 = Regions.split(atlas.findRegion("bulletEnemy1"), 1, 24, 24);
        //this.bulletRegions2 = Regions.split(atlas.findRegion("bulletEnemy2"), 1, 24, 24);
    }

    public void generate(float delta, int frags) {
        level = frags / 10 + 1;
        damageUpMod = (level - 1) / 5 + 1;
        healthUpMod = (level - 1) / 10 + 1;
        bulletUpMod = (level - 1) / 10;
        generateTimer += delta;
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            if (bulletUpMod > 4) {
                bulletUpMod = 4;
            }
            System.out.println("damageUpMod: " + damageUpMod);
            System.out.println("healthUpMod: " + healthUpMod);
            System.out.println("healthUpMod: " + healthUpMod);
            float type = Rnd.nextFloat(0f, 1f);
            EnemyShip enemyShip = enemyPool.obtain();
            if (type < 0.6f) {
                enemyShip.set(
                        enemySmallRegion,
                        enemySmallV,
                        bulletRegions0,
                        ENEMY_SMALL_BULLET_HEIGHT,
                        ENEMY_SMALL_BULLET_VY - ((float)bulletUpMod * 5f),
                        ENEMY_SMALL_DAMAGE * damageUpMod,
                        ENEMY_SMALL_RELOAD_INTERVAL - (((float)bulletUpMod / 10f) * 2f),
                        ENEMY_SMALL_HEIGHT,
                        ENEMY_SMALL_HP * healthUpMod,
                        ENEMY_SMALL_POINTS
                );
            } else if (type < 0.85f) {
                enemyShip.set(
                        enemyAverageRegion,
                        enemyAverageV,
                        bulletRegions0,
                        ENEMY_AVERAGE_BULLET_HEIGHT,
                        ENEMY_AVERAGE_BULLET_VY - ((float)bulletUpMod * 5f),
                        ENEMY_AVERAGE_DAMAGE * damageUpMod,
                        ENEMY_AVERAGE_RELOAD_INTERVAL - (((float)bulletUpMod / 10f) * 2f),
                        ENEMY_AVERAGE_HEIGHT,
                        ENEMY_AVERAGE_HP * healthUpMod,
                        ENEMY_AVERAGE_POINTS
                );
            } else {
                enemyShip.set(
                        enemyBigRegion,
                        enemyBigV,
                        bulletRegions0,
                        ENEMY_BIG_BULLET_HEIGHT,
                        ENEMY_BIG_BULLET_VY - ((float)bulletUpMod * 5f),
                        ENEMY_BIG_DAMAGE * damageUpMod,
                        ENEMY_BIG_RELOAD_INTERVAL - (((float)bulletUpMod / 10f) * 2f),
                        ENEMY_BIG_HEIGHT,
                        ENEMY_BIG_HP * healthUpMod,
                        ENEMY_BIG_POINTS
                );
            }
            enemyShip.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemyShip.getHalfWidth(), worldBounds.getRight() - enemyShip.getHalfWidth());
            enemyShip.setBottom(worldBounds.getTop());
        }
    }

    public int getLevel() {
        return level;
    }

    public int getDamageUpMod() {
        return damageUpMod;
    }

    public int getHealthUpMod() {
        return healthUpMod;
    }
}

