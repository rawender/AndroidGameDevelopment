package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BonusExplosionPool;

public class Bonus extends Sprite {

    private enum State {DESCENT, FIGHT}

    private Rect worldBounds;
    private Vector2 v = new Vector2();
    private Vector2 v0 = new Vector2();
    private Vector2 descentV = new Vector2(0, -15f);
    private State state;
    private BonusExplosionPool bonusExplosionPool;
    private int hp;
    private String type;
    private float damageAnimateInterval = 0.1f;
    private float damageAnimateTimer;

    public Bonus(BonusExplosionPool bonusExplosionPool, Rect worldBounds) {
        this.bonusExplosionPool = bonusExplosionPool;
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        this.pos.mulAdd(v, delta);
        damageAnimateTimer += delta;
        if (damageAnimateTimer >= damageAnimateInterval) {
            frame = 0;
        }
        switch (state) {
            case DESCENT:
                if (getTop() <= worldBounds.getTop()) {
                    v.set(v0);
                    state = State.FIGHT;
                }
                break;
            case FIGHT:
                if (getBottom() <= worldBounds.getBottom()) {
                    this.destroy();
                }
                break;
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            int hp,
            String type,
            float height
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.hp = hp;
        this.type = type;
        setHeightProportion(height);
        this.v.set(descentV);
        state = State.DESCENT;
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(
                bullet.getRight() < getLeft()
                        || bullet.getLeft() > getRight()
                        || bullet.getBottom() > getTop()
                        || bullet.getTop() <  pos.y
        );
    }

    public void damage(int damage) {
        frame = 1;
        damageAnimateTimer = 0f;
        hp -= damage;
        if (hp <= 0) {
            destroy();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    public void boom() {
        BonusExplosion bonusExplosion = bonusExplosionPool.obtain();
        bonusExplosion.set(this.getHeight() * 2f, this.pos);
    }

    public String getType() {
        return type;
    }


    public boolean cheсkState() {
        boolean res = true;
        if (this.state == State.DESCENT) {
            res = false;
        }
        return res;
    }
}
