package ru.geekbrains.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.BonusPool;
import ru.geekbrains.sprite.Bonus;

public class BonusEmitter {

    private static final float BONUS_HEIGHT = 5f;
    private static final int BONUS_HP = 5;
    private static final String HEAL_BONUS_TYPE = "heal";
    private static final String SHIELD_BONUS_TYPE = "shield";
    private static final String DAMAGE_BONUS_TYPE = "damage";

    private Rect worldBounds;

    private float generateInterval = 30f;
    private float generateTimer;

    private TextureRegion[] healBonusRegion;
    private Vector2 healBonusV = new Vector2(0f, -10f);
    private TextureRegion[] shieldBonusRegion;
    private Vector2 shieldBonusV = new Vector2(0f, -12.5f);
    private TextureRegion[] damageBonusRegion;
    private Vector2 damageBonusV = new Vector2(0f, -15f);

    private BonusPool bonusPool;

    public BonusEmitter(TextureAtlas atlas, Rect worldBounds, BonusPool bonusPool) {
        this.worldBounds = worldBounds;
        this.bonusPool = bonusPool;
        TextureRegion textureRegion0 = atlas.findRegion("Bonus0");
        this.healBonusRegion = Regions.split(textureRegion0, 1, 2, 2);
        TextureRegion textureRegion1 = atlas.findRegion("Bonus1");
        this.damageBonusRegion = Regions.split(textureRegion1, 1, 2, 2);
        TextureRegion textureRegion2 = atlas.findRegion("Bonus2");
        this.shieldBonusRegion = Regions.split(textureRegion2, 1, 2, 2);
    }

    public void generate(float delta,  int healthUpMod, int damageUpMod, int health, int damage) {
        generateTimer += delta;
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            float type = Rnd.nextFloat(0f, 1f);
            if (type < 0.15f) {
                Bonus bonus = bonusPool.obtain();
                bonus.set(
                        shieldBonusRegion,
                        shieldBonusV,
                        BONUS_HP * healthUpMod,
                        SHIELD_BONUS_TYPE,
                        BONUS_HEIGHT
                );
                bonus.pos.x = Rnd.nextFloat(worldBounds.getLeft() + bonus.getHalfWidth(), worldBounds.getRight() - bonus.getHalfWidth());
                bonus.setBottom(worldBounds.getTop());
            } else if (type < 0.3f) {
                if (health < 50) {
                    Bonus bonus = bonusPool.obtain();
                    bonus.set(
                            healBonusRegion,
                            healBonusV,
                            BONUS_HP * healthUpMod,
                            HEAL_BONUS_TYPE,
                            BONUS_HEIGHT
                    );
                    bonus.pos.x = Rnd.nextFloat(worldBounds.getLeft() + bonus.getHalfWidth(), worldBounds.getRight() - bonus.getHalfWidth());
                    bonus.setBottom(worldBounds.getTop());
                }
            } else if (type < 0.45f) {
                if (damage < damageUpMod) {
                    Bonus bonus = bonusPool.obtain();
                    bonus.set(
                            damageBonusRegion,
                            damageBonusV,
                            BONUS_HP * healthUpMod,
                            DAMAGE_BONUS_TYPE,
                            BONUS_HEIGHT
                    );
                    bonus.pos.x = Rnd.nextFloat(worldBounds.getLeft() + bonus.getHalfWidth(), worldBounds.getRight() - bonus.getHalfWidth());
                    bonus.setBottom(worldBounds.getTop());
                }
            }
        }
    }
}
