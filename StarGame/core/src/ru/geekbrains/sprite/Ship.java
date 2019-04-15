package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;

public class Ship extends Sprite {

    protected Rect worldBounds;

    protected Vector2 v = new Vector2();
    protected Vector2 bulletV = new Vector2();
    protected float bulletHeight;

    protected BulletPool bulletPool;
    protected ExplosionPool explosionPool;
    protected TextureRegion[] bulletRegions;
    protected int damage;
    protected int hp;

    protected Sound shootSound;

    protected float reloadInterval;
    protected float reloadTimer;

    private float damageAnimateInterval = 0.1f;
    private float damageAnimateTimer;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        this.pos.mulAdd(v, delta);
        damageAnimateTimer += delta;
        if (damageAnimateTimer >= damageAnimateInterval) {
            frame = 0;
        }
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

    public void shoot() {
        Bullet bullets = bulletPool.obtain();
        bullets.set(this, bulletRegions, pos, bulletV, bulletHeight, worldBounds, damage);
        shootSound.play();
    }

    public void boom() {
        Explosion explosion = explosionPool.obtain();
        explosion.set(this.getHeight() * 2f, this.pos);
    }

    public int getHp() {
        return hp;
    }

    public Vector2 getV() {
        return v;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}

