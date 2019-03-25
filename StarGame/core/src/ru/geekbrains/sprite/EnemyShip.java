package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;

public class EnemyShip extends Ship {

    private enum State {DESCENT, FIGHT}

    private Vector2 v0 = new Vector2();
    private Vector2 descentV = new Vector2(0, -15f);
    private State state;

    public EnemyShip(BulletPool bulletPool, Rect worldBounds, Sound shootSound) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.shootSound = shootSound;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        switch (state) {
            case DESCENT:
                if (getTop() <= worldBounds.getTop()) {
                    v.set(v0);
                    state = State.FIGHT;
                }
                break;
            case FIGHT:
                reloadTimer += delta;
                if (reloadTimer >= reloadInterval) {
                    reloadTimer = 0f;
                    shoot();
                }
                if (getBottom() <= worldBounds.getBottom()) {
                    this.destroy();
                }
                break;
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int damage,
            float reloadInterval,
            float height,
            int hp
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.hp = hp;
        setHeightProportion(height);
        reloadTimer = reloadInterval;
        this.v.set(descentV);
        state = State.DESCENT;
    }
}
