package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class EnemyShip extends Sprite {

    private Rect worldBounds;
    private Vector2 shipSpeed;
    private int hitPoints;
    private int damage;
    private Vector2 bulletSpeed;

    public EnemyShip() {
        this.shipSpeed = new Vector2();
        this.regions = new TextureRegion[1];
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(shipSpeed, delta);
        if (isOutside(worldBounds)) {
            destroy();
        }
    }

    public void set(
            TextureRegion region,
            Vector2 pos0,
            Vector2 v0,
            int hp,
            int damage,
            Vector2 vb,
            Rect worldBounds,
            float height
    ) {
        this.regions[0] = region;
        this.pos.set(pos0);
        this.shipSpeed.set(v0);
        this.hitPoints = hp;
        this.damage = damage;
        this.bulletSpeed = vb;
        this.worldBounds = worldBounds;
        setHeightProportion(height);
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getDamage() {
        return damage;
    }

    public Vector2 getBulletSpeed() {
        return bulletSpeed;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setBulletSpeed(Vector2 bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }
}
