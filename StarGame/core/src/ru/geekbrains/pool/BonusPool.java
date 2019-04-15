package ru.geekbrains.pool;

import ru.geekbrains.base.SpritesPool;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Bonus;

public class BonusPool extends SpritesPool<Bonus>{

    private BonusExplosionPool bonusExplosionPool;
    private Rect worldBounds;

    public BonusPool(BonusExplosionPool bonusExplosionPool, Rect worldBounds) {
        this.bonusExplosionPool = bonusExplosionPool;
        this.worldBounds = worldBounds;
    }
    @Override
    protected Bonus newObject() {
        return new Bonus(bonusExplosionPool, worldBounds);
    }
}
